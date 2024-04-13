package com.lxsc.orders.config;

public class AlipayConfig {
    // 商户appid
    public static String APPID = "9021000123631898";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCnV0A/yKK6/KPz3UpnJ4aTa/xVn1yzDQpfYbE5CMDwJSDJWf2VegxFU1cdK/iTc1tu25tfdxX16be2r4KYHSnN9R4i9lrnXlwn5qZcA9tY+NkwDisi0CCKOJlIXc6RBY+e6Vng0sIiZFjxsrdfSSE46+PK7W1HwXWtZHrRYEXKeZFRTGKY1LNWUlT784J7YBkEARlrd/SqNc0H5BZV7bGOMHkb+NsPc5VgbahHRmfJw72+opY5mtV+pEkOxjm6xeXOPhw7WPuJMcNFW/T0dp+o9sihsAbNRvLDd4xUHEZK9ZdFrHRRMvcoOTbj7m6NBTenqYwLKQDsAeSCFjd4HYbfAgMBAAECggEAVqgWO3GY46AE/zovV2/efuVCzMwz4vzzzCT484IW98UiT2LssMV9KUFZMWAd362zQZszwKIYmzomytHknQUtKtyueIh+1qXEr1z/9VKB+ncTcZFEwUhqMWnR5um9GNh6YV6SDpzVr52rea6EyzFBpQpy8hk1qB0E5X/NIjPqsEidQ9TIGyt3hzMFBLTsXjCbY9/e4YpMYuFmDHMobHYy+QoxkjKbn0XA8kmjO84fXUCK5pBxgozsbCfZHNDS+knekB27TF3uFq8CMGkAYY3KsbypS7j8PJn68MSM257ml2WmJFj8+jUw3CQsCoN/BFsC2+81cvNZYjOjvt6CeJjRsQKBgQD9Dw3rk9eiVcp2qgBK1HeVQpGdCXJqPYyiuCFDmYX2oTvmQ5UnhPqWvZxGhMT5xl5ncros8QG06P87IkTQUTFw4lZKYuNog3Oy6ocBslYjW1iDLRb12D0hTfDTcC/IAPRUzG8h2lzhLH78JzvoZ+ltZCsIPXOuTudtkouZnp6aawKBgQCpSSc3pGTa6ZxHUrRvX1yL/TaPNw/cPmw9WgaVm+ePmng2XPOkMvJJglkC+f97eZkVAnUbGUTguyW3Vp+OSkT13VCZRWNv5ZXOlTEfK6HCK+uUuzMoik0IAXDVtDUh5eudP4K9S+3xxByH1pLr8tsTXWl5XNOJwfOaQ5nAz7XKXQKBgQDlcGzSluz/xoEXLfOt5Q23Su/zTclrAfss3X1raRrJDk7c65GkjzEU0z2Jwn9FZ9HYQXEDplBB0q720o9JwRPuoYhTUW2WKPkR0wotQ6BN1lheNteGd1SVibE8Q49O626cI+7u5qknj8ximr/6a5x9DkDl67Gi7O9vrVrdIa/zmQKBgAjexmlJ+ebmmsO5965mRsknCXbTocceljlYwEOSmb2SkH34vON7r+peJB/dzZ9Ard7F9DDpUAyegclrhozEI3zmjdSaKC2yz/i1JI/Hj4BHIN74OCQFqyeiO7FW8fnGvGPO8iDNG7ixV/VeLvCiFwyatm4fiQ4M1aDdre7Gc3YZAoGBAIOlqSXbcOs6i8X1GF/GGGuS+29pWqrFPWiq0fM1tY29RzpOmsOeGAqh74VX/R/JyoaE/5xpawtBzSSKKG0IInVcbBwXsRTKwYW0Z+VKmzfW7i2ym41A4yUPiwyIpF9XW4bvZuE7ui3PTbtlB0o2LlySnqyeCDZRQQEZoru5Rjqt";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8083/notify_url";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "http://localhost:8083/paySuccess";
    // 请求网关地址,真实地址
    //public static String URL = "https://openapi.alipay.com/gateway.do";
    public static String URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAshfJevlbYylnlBjgnSSkpnn2qDSLK6RHwE2gI6XKRNS+YJ9Je1UAhDUjBzKjFrmKHCzBWK2gfj0/7TSPObiiohNIvnbge/sQlgdGYbJ5CmyLyFYTj1X6ahpXkOJiI7acRa2a9zWc8amgoz2ywR7x0DaiNhTCtylg4qO8FNXW7U77Za8JdvoFW00r15UCvZQ3zi8BdUK+dtCQwQjdOipqbuZTTEI4woKnWQSRoEq+QsimckQR02bJStH/KByqGj+/2l16Ji1hbuKjf2ifqAjg3K+NoJSd0obge6HYS3Xr7lxRfxLcv03ZNzWV0r8d8nVFJwo/rStbFGcSlVLgca0y8wIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";
}
