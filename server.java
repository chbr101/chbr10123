async function updateClientUserDeviceInfo() {
    if (navigator.userAgentData && navigator.userAgentData.platform) {
        try {
            // 플랫폼 버전과 브라우저 정보를 가져옴
            const ua = await navigator.userAgentData.getHighEntropyValues(['platformVersion', 'fullVersionList']);

            // 플랫폼 버전 및 플랫폼 이름 설정
            clientUserDeviceInfo.platformVersion = ua.platformVersion; // 예: "11.0.22000.71"
            clientUserDeviceInfo.platform = navigator.userAgentData.platform; // 예: "Windows", "macOS"

            // UA-CH를 지원하는 브라우저 목록 (Chromium 기반 브라우저)
            const supportedBrowsers = ["Google Chrome", "Microsoft Edge", "Opera", "Samsung Internet", "Brave"];

            // 브라우저 정보 설정
            const browserInfo = ua.fullVersionList.find(item => supportedBrowsers.includes(item.brand));

            // 브라우저 이름과 버전을 설정
            clientUserDeviceInfo.browser = browserInfo ? browserInfo.brand : null;
            clientUserDeviceInfo.browserVersion = browserInfo ? browserInfo.version : null;

            // 이후 서버로 전송하는 로직은 기존에 있다고 가정하고 생략

        } catch (error) {
            console.error('Failed to retrieve platform version or browser info:', error);
        }
    } else {
        // UA-CH를 지원하지 않는 경우에도 null로 설정
        clientUserDeviceInfo.browser = null;
        clientUserDeviceInfo.browserVersion = null;
    }
}