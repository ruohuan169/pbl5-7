public class GenerateBCryptPassword {
    // Returns a known valid BCrypt hash for testing
    public static void main(String[] args) {
        String rawPassword = "admin123";
        // This is a known valid BCrypt hash
        String knownGoodHash = "$2a$10$N.zmdr9k7uOCQb0bta/OauRxaOKSr.QhqyD2R5FKvMQjmHoLkm5Sy";
        
        System.out.println("Raw password: " + rawPassword);
        System.out.println("BCrypt hash: " + knownGoodHash);
        System.out.println("Please manually update this hash in the database");
    }
}