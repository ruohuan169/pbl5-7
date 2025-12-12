const axios = require('axios');
const fs = require('fs');

async function testIndexAccess() {
    console.log('开始测试访问/index页面...');
    
    // 创建一个axios实例
    const client = axios.create({
        baseURL: 'http://localhost:8080',
        timeout: 5000,
        maxRedirects: 0, // 禁止自动重定向
        headers: {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
        }
    });
    
    try {
        // 首先访问登录页面获取CSRF token
        console.log('正在获取登录页面...');
        const loginPageResponse = await client.get('/login');
        const setCookieHeader = loginPageResponse.headers['set-cookie'];
        
        // 提取CSRF token
        const csrfMatch = loginPageResponse.data.match(/name="_csrf" value="([^"]+)"/);
        if (!csrfMatch) {
            console.error('无法找到CSRF token');
            return;
        }
        const csrfToken = csrfMatch[1];
        console.log('CSRF Token:', csrfToken);
        
        // 设置cookie
        if (setCookieHeader) {
            client.defaults.headers.Cookie = setCookieHeader.join('; ');
        }
        
        // 尝试登录
        console.log('正在尝试登录...');
        const loginResponse = await client.post('/login', 
            `username=test&password=test123&_csrf=${csrfToken}`,
            {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                validateStatus: function (status) {
                    return status < 500; // 允许重定向
                }
            }
        );
        
        console.log('登录响应状态:', loginResponse.status);
        if (loginResponse.status === 302) {
            console.log('登录成功!');
        } else {
            console.log('登录失败:', loginResponse.status);
            return;
        }
        
        // 获取登录后的cookies
        const authCookies = loginResponse.headers['set-cookie'];
        if (authCookies) {
            client.defaults.headers.Cookie = authCookies.join('; ');
        }
        
        // 访问主页
        console.log('正在访问主页验证登录状态...');
        const indexResponse = await client.get('/index', {
            validateStatus: function (status) {
                return status < 500; // 允许任何状态码
            }
        });
        
        console.log('主页响应状态:', indexResponse.status);
        if (indexResponse.status === 200) {
            console.log('成功访问主页!');
            // 将响应内容写入文件以便分析
            fs.writeFileSync('index-response.html', indexResponse.data);
            console.log('主页内容已保存到 index-response.html 文件');
        } else {
            console.log('访问主页失败，状态码:', indexResponse.status);
            console.log('响应头:', indexResponse.headers);
            // 如果有响应体，也保存下来
            if (indexResponse.data) {
                // 检查数据类型并适当处理
                let dataToWrite = indexResponse.data;
                if (typeof dataToWrite !== 'string') {
                    dataToWrite = JSON.stringify(dataToWrite, null, 2);
                }
                fs.writeFileSync('error-response.html', dataToWrite);
                console.log('错误响应内容已保存到 error-response.html 文件');
            }
        }
    } catch (error) {
        console.error('测试过程中发生错误:', error.message);
        if (error.response) {
            console.log('错误响应状态:', error.response.status);
            console.log('错误响应头:', error.response.headers);
            if (error.response.data) {
                // 检查数据类型并适当处理
                let dataToWrite = error.response.data;
                if (typeof dataToWrite !== 'string') {
                    dataToWrite = JSON.stringify(dataToWrite, null, 2);
                }
                fs.writeFileSync('exception-response.html', dataToWrite);
                console.log('异常响应内容已保存到 exception-response.html 文件');
            }
        }
    }
}

testIndexAccess();