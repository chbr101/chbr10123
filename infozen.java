async function updateClientUserDeviceInfo() {
    // UA-CH를 지원하는지 확인. UA-CH는 최신 브라우저에서 사용 가능
    if (navigator.userAgentData) {
        try {
            // 'platformVersion'과 'fullVersionList'를 가져오기 위해 getHighEntropyValues 메서드를 호출
            // 이 메서드는 OS 버전 정보와 브라우저의 세부 버전 정보를 반환
            const ua = await navigator.userAgentData.getHighEntropyValues(['platformVersion', 'fullVersionList']);

            // 예시: platformVersion = "10.15.7" 또는 "11.0.1"
            // 'platformVersion'을 '.'으로 분리하여 major, minor, build 버전 정보 추출
            const versionParts = ua.platformVersion.split('.');
            const majorPlatformVersion = parseInt(versionParts[0], 10); // 운영체제의 주요 버전 정보 (예: 10, 11)
            const minorPlatformVersion = parseInt(versionParts[1], 10); // 운영체제의 마이너 버전 정보 (예: 15, 0)
            // platformName: 운영체제 이름 (예: "Windows", "macOS")
            const platformName = navigator.userAgentData.platform; 

            // 추출한 플랫폼 이름과 버전을 클라이언트 디바이스 정보에 저장
            // 예: platform = "macOS", platformVersion = "10.15.7"
            clientUserDeviceInfo.platform = platformName;
            clientUserDeviceInfo.platformVersion = ua.platformVersion;

            // Windows 판별 로직
            if (platformName === 'Windows') {
                // Windows 11 이상의 버전은 major version이 13 이상일 경우로 판별 (예: "10.0.22000")
                if (majorPlatformVersion >= 13) {
                    console.log("Windows 11 or later"); // Windows 11 이상임을 출력
                    clientUserDeviceInfo.isWindows11 = true; // Windows 11 이상임을 기록
                } else if (majorPlatformVersion === 10) {
                    console.log("Windows 10"); // Windows 10임을 출력
                    clientUserDeviceInfo.isWindows11 = false; // Windows 10임을 기록
                } else {
                    console.log("Before Windows 10"); // Windows 10 이하임을 출력
                    clientUserDeviceInfo.isWindows11 = false; // Windows 10 이하임을 기록
                }
            } 
            // macOS 판별 로직
            else if (platformName === 'macOS') {
                // macOS Catalina 이후 버전인지 판별 (major version > 10 또는 major version이 10이고 minor version이 15 이상)
                // 예: majorPlatformVersion = 10, minorPlatformVersion = 15 -> Catalina 이후
                if (majorPlatformVersion > 10 || (majorPlatformVersion === 10 && minorPlatformVersion >= 15)) {
                    console.log("macOS Catalina 이후 버전"); // macOS Catalina 이후 버전임을 출력
                    clientUserDeviceInfo.isPostCatalina = true; // macOS Catalina 이후 버전임을 기록
                } else {
                    console.log("macOS Catalina 이전 버전"); // macOS Catalina 이전 버전임을 출력
                    clientUserDeviceInfo.isPostCatalina = false; // macOS Catalina 이전 버전임을 기록
                }
            } else {
                // Windows나 macOS가 아닌 경우 출력
                console.log("Not running on Windows or macOS");
            }

            // UA-CH를 지원하는 브라우저 목록 (Chromium 기반 브라우저)
            const supportedBrowsers = ["Google Chrome", "Microsoft Edge", "Opera", "Samsung Internet", "Brave"];

            // fullVersionList는 사용 중인 브라우저의 이름과 버전 정보를 제공
            // 예시: fullVersionList = [{brand: "Google Chrome", version: "90.0.4430.93"}, {...}]
            // 지원되는 브라우저 목록에서 현재 브라우저의 정보(이름 및 버전)를 찾음
            const browserInfo = ua.fullVersionList.find(item => supportedBrowsers.includes(item.brand));

            // 브라우저 정보가 존재하면 클라이언트 디바이스 정보에 저장
            if (browserInfo) {
                // 예: browserInfo.brand = "Google Chrome", browserInfo.version = "90.0.4430.93"
                clientUserDeviceInfo.browser = browserInfo.brand; // 브라우저 이름을 저장
                clientUserDeviceInfo.browserVersion = browserInfo.version; // 브라우저 버전을 저장
                console.log(`Browser: ${browserInfo.brand}, Version: ${browserInfo.version}`); // 브라우저 정보 출력
            } else {
                // 브라우저 정보가 없거나 UA-CH를 지원하지 않는 브라우저의 경우
                clientUserDeviceInfo.browser = "Unsupported browser or UA-CH not supported";
                clientUserDeviceInfo.browserVersion = null;
                console.log('UA-CH를 지원하지 않는 브라우저입니다.');
            }

        } catch (error) {
            // 플랫폼 버전 또는 브라우저 정보를 가져오는 데 실패한 경우 에러 메시지 출력
            console.error('플랫폼 버전 또는 브라우저 정보를 가져오지 못했습니다:', error);
        }
    } else {
        // UA-CH를 사용할 수 없는 경우 (예: 비 Chromium 기반 브라우저)
        console.log('navigator.userAgentData를 사용할 수 없습니다. 이 방법은 Chromium 기반 브라우저에서만 지원됩니다.');
        clientUserDeviceInfo.browser = null;
        clientUserDeviceInfo.browserVersion = null;
    }
}
