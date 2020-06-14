package hr.dstr89.demo.core.mocks;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.sling.settings.SlingSettingsService;

import com.day.cq.commons.Externalizer;

public class MockSlingSettingsService implements SlingSettingsService {

    @Override
    public String getAbsolutePathWithinSlingHome(final String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSlingId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSlingHomePath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URL getSlingHome() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getRunModes() {
        return new HashSet<>(Arrays.asList(Externalizer.AUTHOR, Externalizer.PUBLISH, Externalizer.LOCAL));
    }

    @Override
    public String getSlingName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSlingDescription() {
        throw new UnsupportedOperationException();
    }

}
