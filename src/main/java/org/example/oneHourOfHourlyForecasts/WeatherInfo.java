
package org.example.oneHourOfHourlyForecasts;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "DateTime",
    "EpochDateTime",
    "WeatherIcon",
    "IconPhrase",
    "HasPrecipitation",
    "IsDaylight",
    "Temperature",
    "PrecipitationProbability",
    "MobileLink",
    "Link"
})

public class WeatherInfo {

    @JsonProperty("DateTime")
    private String dateTime;
    @JsonProperty("EpochDateTime")
    private Integer epochDateTime;
    @JsonProperty("WeatherIcon")
    private Integer weatherIcon;
    @JsonProperty("IconPhrase")
    private String iconPhrase;
    @JsonProperty("HasPrecipitation")
    private Boolean hasPrecipitation;
    @JsonProperty("IsDaylight")
    private Boolean isDaylight;
    @JsonProperty("Temperature")
    private Temperature temperature;
    @JsonProperty("PrecipitationProbability")
    private Integer precipitationProbability;
    @JsonProperty("MobileLink")
    private String mobileLink;
    @JsonProperty("Link")
    private String link;

    @JsonProperty("DateTime")
    public String getDateTime() {
        return dateTime;
    }

    @JsonProperty("DateTime")
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @JsonProperty("EpochDateTime")
    public Integer getEpochDateTime() {
        return epochDateTime;
    }

    @JsonProperty("EpochDateTime")
    public void setEpochDateTime(Integer epochDateTime) {
        this.epochDateTime = epochDateTime;
    }

    @JsonProperty("WeatherIcon")
    public Integer getWeatherIcon() {
        return weatherIcon;
    }

    @JsonProperty("WeatherIcon")
    public void setWeatherIcon(Integer weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    @JsonProperty("IconPhrase")
    public String getIconPhrase() {
        return iconPhrase;
    }

    @JsonProperty("IconPhrase")
    public void setIconPhrase(String iconPhrase) {
        this.iconPhrase = iconPhrase;
    }

    @JsonProperty("HasPrecipitation")
    public Boolean getHasPrecipitation() {
        return hasPrecipitation;
    }

    @JsonProperty("HasPrecipitation")
    public void setHasPrecipitation(Boolean hasPrecipitation) {
        this.hasPrecipitation = hasPrecipitation;
    }

    @JsonProperty("IsDaylight")
    public Boolean getIsDaylight() {
        return isDaylight;
    }

    @JsonProperty("IsDaylight")
    public void setIsDaylight(Boolean isDaylight) {
        this.isDaylight = isDaylight;
    }

    @JsonProperty("Temperature")
    public Temperature getTemperature() {
        return temperature;
    }

    @JsonProperty("Temperature")
    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    @JsonProperty("PrecipitationProbability")
    public Integer getPrecipitationProbability() {
        return precipitationProbability;
    }

    @JsonProperty("PrecipitationProbability")
    public void setPrecipitationProbability(Integer precipitationProbability) {
        this.precipitationProbability = precipitationProbability;
    }

    @JsonProperty("MobileLink")
    public String getMobileLink() {
        return mobileLink;
    }

    @JsonProperty("MobileLink")
    public void setMobileLink(String mobileLink) {
        this.mobileLink = mobileLink;
    }

    @JsonProperty("Link")
    public String getLink() {
        return link;
    }

    @JsonProperty("Link")
    public void setLink(String link) {
        this.link = link;
    }

}
