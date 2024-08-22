// UA-CH에서 전송된 값들을 처리하는 서버 측 코드
public void processClientUserDeviceInfo(String clientOs, String platformVersion, String browser, String browserVersion, UserAgent parsedUserAgent) {
    // 예: clientOs는 "Windows", "macOS" 등, platformVersion은 "11.0.22000.71", "10.15.7" 등
    // browser는 "Google Chrome", "Microsoft Edge" 등, browserVersion은 "91.0.4472.124" 등

    // 브라우저 정보가 null인 경우, 이미 파싱된 User-Agent 정보를 사용하여 브라우저 정보를 설정
    if (browser == null || browserVersion == null) {
        if (parsedUserAgent != null) {
            browser = parsedUserAgent.getBrowserName(); // 예: "Google Chrome", "Firefox", "Safari"
            browserVersion = parsedUserAgent.getBrowserVersion(); // 예: "91.0.4472.124", "89.0", "14.1.2"
        }
    }

    // Windows OS에서 Windows 11을 판별하여 설정
    if ("Windows".equalsIgnoreCase(clientOs)) {
        if (platformVersion != null) {
            String[] versionParts = platformVersion.split("\\.");
            int majorPlatformVersion = Integer.parseInt(versionParts[0]);

            if (majorPlatformVersion >= 13) { // Windows 11 또는 그 이상
                device.setOsVersion(11); // Windows 11
            }
        }
    }

    // macOS에서 Catalina 이후 버전을 판별하여 설정 (Catalina 제외)
    else if ("macOS".equalsIgnoreCase(clientOs)) {
        if (platformVersion != null) {
            String[] versionParts = platformVersion.split("\\.");
            int majorVersion = Integer.parseInt(versionParts[0]);
            int minorVersion = versionParts.length > 1 ? Integer.parseInt(versionParts[1]) : 0;

            // Catalina(10.15) 이후 버전인지 판별 (Catalina는 제외)
            if (majorVersion > 10 || (majorVersion == 10 && minorVersion > 15)) {
                device.setOsVersion(majorVersion); // 예: 11, 12 등
            }
        }
    }

    // 최종적으로 브라우저 정보와 운영체제 정보를 device 객체에 설정
    device.setBrowser(browser);
    device.setBrowserVersion(browserVersion);
}