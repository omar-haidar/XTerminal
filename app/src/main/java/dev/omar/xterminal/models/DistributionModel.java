package dev.omar.xterminal.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DistributionModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("name")
    private String name;

    @SerializedName("icon")
    private String icon;

    @SerializedName("url")
    private String url;

    @SerializedName("architectures")
    private List<String> architectures = new ArrayList<>();

    @SerializedName("releases")
    private List<Releases> releases = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setArchitectures(List<String> architectures) {
        this.architectures = architectures;
    }

    public List<String> getArchitectures() {
        return architectures;
    }

    public void setReleases(List<Releases> releases) {
        this.releases = releases;
    }

    public List<Releases> getReleases() {
        return releases;
    }

    @Override
    public String toString() {
        return "DistributionModel{"
                + "name=" + name
                + ", icon=" + icon
                + ", url=" + url
                + ", architectures=" + architectures
                + ", releases=" + releases
                + "}";
    }

    public static class Releases implements Serializable {

        private static final long serialVersionUID = 1L;

        @SerializedName("name")
        private String name;

        @SerializedName("version")
        private String version;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVersion() {
            return version;
        }

        @Override
        public String toString() {
            return "Releases{"
                    + "name=" + name
                    + ", version=" + version
                    + "}";
        }

    }

}