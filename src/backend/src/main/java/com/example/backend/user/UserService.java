package com.example.backend.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public static String getMD5(String input, String salt) {
        try {
            input = input + salt; // 将密码和盐结合
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    @Transactional
    public void addUser(User user) {
        user.setPassword(getMD5(user.getPassword(),"我是增加的辅助信息"));
        userMapper.insertUser(user);
    }

    public boolean loginUser(String username, String password) {
        User user = userMapper.findUserByUsername(username);
        if (user != null) {
            // 验证密码
            String saltedPassword = getMD5(password, "我是增加的辅助信息");
            return saltedPassword.equals(user.getPassword());
        }
        return false;
    }

    public User getUserInfoByToken(String token) throws Exception {
        // 解析Token获取用户名
        String username = JwtUtil.getUsernameFromToken(token);

        // 根据用户名获取用户信息
        User user = userMapper.findUserByUsername(username);
        if (user == null) {
            throw new Exception("用户不存在");
        }
        return user;
    }
    @Transactional
    public void update(User user) {
        userMapper.updateUser(user);
    }
}
