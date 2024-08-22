async function updateClientUserDeviceInfo() {
    if (navigator.userAgentData) {
        try {
            const ua = await navigator.userAgentData.getHighEntropyValues(['platformVersion', 'fullVersionList']);

            const versionParts = ua.platformVersion.split('.');
            const majorPlatformVersion = parseInt(versionParts[0], 10); // 운영체제의 주요 버전 정보 추출
            const minorPlatformVersion = parseInt(versionParts[1], 10); // 운영체제의 마이너 버전 정보 추출
            const platformName = navigator.userAgentData.platform; // 플랫폼 이름 (Windows, macOS 등)

            // 플랫폼 이름과 버전 설정
            clientUserDeviceInfo.platform = platformName;
            clientUserDeviceInfo.platformVersion = ua.platformVersion;

            // Windows 판별 로직
            if (platformName === 'Windows') {
                if (majorPlatformVersion >= 13) {
                    console.log("Windows 11 or later");
                    clientUserDeviceInfo.isWindows11 = true;
                } else if (majorPlatformVersion === 10) {
                    console.log("Windows 10");
                    clientUserDeviceInfo.isWindows11 = false;
                } else {
                    console.log("Before Windows 10");
                    clientUserDeviceInfo.isWindows11 = false;
                }
            } 
            // macOS 판별 로직
            else if (platformName === 'macOS') {
                if (majorPlatformVersion > 10 || (majorPlatformVersion === 10 && minorPlatformVersion >= 15)) {
                    console.log("macOS Catalina 이후 버전");
                    clientUserDeviceInfo.isPostCatalina = true;
                } else {
                    console.log("macOS Catalina 이전 버전");
                    clientUserDeviceInfo.isPostCatalina = false;
                }
            } else {
                console.log("Not running on Windows or macOS");
            }

            // UA-CH를 지원하는 브라우저 목록 (Chromium 기반 브라우저)
            const supportedBrowsers = ["Google Chrome", "Microsoft Edge", "Opera", "Samsung Internet", "Brave"];

            // 브라우저 정보 설정
            const browserInfo = ua.fullVersionList.find(item => supportedBrowsers.includes(item.brand));

            if (browserInfo) {
                clientUserDeviceInfo.browser = browserInfo.brand;
                clientUserDeviceInfo.browserVersion = browserInfo.version;
                console.log(`Browser: ${browserInfo.brand}, Version: ${browserInfo.version}`);
            } else {
                clientUserDeviceInfo.browser = "Unsupported browser or UA-CH not supported";
                clientUserDeviceInfo.browserVersion = null;
                console.log('UA-CH를 지원하지 않는 브라우저입니다.');
            }

        } catch (error) {
            console.error('플랫폼 버전 또는 브라우저 정보를 가져오지 못했습니다:', error);
        }
    } else {
        console.log('navigator.userAgentData를 사용할 수 없습니다. 이 방법은 Chromium 기반 브라우저에서만 지원됩니다.');
        clientUserDeviceInfo.browser = null;
        clientUserDeviceInfo.browserVersion = null;
    }
}
