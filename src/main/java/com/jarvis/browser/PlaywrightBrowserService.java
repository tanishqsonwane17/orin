package com.jarvis.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "jarvis.browser", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PlaywrightBrowserService implements BrowserService {

    @Override
    public void openUrl(String url) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();
            page.navigate(ensureScheme(url));
        }
    }

    @Override
    public void search(String query) {
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        openUrl("https://www.google.com/search?q=" + encoded);
    }

    private String ensureScheme(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        return "https://" + url;
    }
}
