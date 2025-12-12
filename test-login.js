const axios = require('axios');
const cheerio = require('cheerio');

async function testLogin() {
  // 创建一个实例，启用 cookie 支持
  const instance = axios.create({
    baseURL: 'http://localhost:8080',
    withCredentials: true
  });

  try {
    console.log('开始测试登录流程...');
    
    // 1. 获取登录页面以获得 CSRF token
    console.log('正在获取登录页面...');
    const loginPageResponse = await instance.get('/login');
    const $ = cheerio.load(loginPageResponse.data);
    const csrfToken = $('input[name="_csrf"]').val();
    
    console.log('CSRF Token:', csrfToken);
    
    if (!csrfToken) {
      console.error('未找到 CSRF token');
      return;
    }

    // 2. 尝试使用 admin/admin123 登录
    console.log('正在尝试登录...');
    const loginResponse = await instance.post('/login', 
      new URLSearchParams({
        'username': 'admin',
        'password': 'admin123',
        '_csrf': csrfToken
      }).toString(),
      {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        maxRedirects: 0,
        validateStatus: function (status) {
          // 允许重定向状态码 (3xx)
          return status < 500;
        }
      }
    );

    console.log('登录响应状态:', loginResponse.status);
    
    // 检查是否登录成功 (302 重定向表示成功)
    if (loginResponse.status === 302 || loginResponse.status === 200) {
      console.log('登录成功!');
      
      // 3. 访问主页验证是否真的登录成功
      console.log('正在访问主页验证登录状态...');
      const indexResponse = await instance.get('/index');
      console.log('主页响应状态:', indexResponse.status);
      
      if (indexResponse.status === 200) {
        const titleMatch = indexResponse.data.match(/<title>(.*?)<\/title>/);
        const title = titleMatch ? titleMatch[1] : '未知标题';
        console.log('主页标题:', title);
        console.log('完全登录成功!');
      }
    } else {
      console.log('登录失败，状态码:', loginResponse.status);
      if (loginResponse.data) {
        console.log('响应内容预览:', loginResponse.data.substring(0, 200));
      }
    }
    
  } catch (error) {
    console.error('发生错误:', error.message);
    if (error.response) {
      console.error('错误响应状态:', error.response.status);
      console.error('错误响应头:', error.response.headers);
      if (error.response.data) {
        console.error('错误响应数据预览:', error.response.data.toString().substring(0, 200));
      }
    }
  }
}

// 执行测试
testLogin();