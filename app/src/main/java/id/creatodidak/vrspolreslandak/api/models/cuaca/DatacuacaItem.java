package id.creatodidak.vrspolreslandak.api.models.cuaca;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DatacuacaItem{

	@SerializedName("update")
	private String update;

	@SerializedName("kabupaten")
	private String kabupaten;

	@SerializedName("humidity_min")
	private List<HumidityMinItem> humidityMin;

	@SerializedName("temperature_min")
	private List<TemperatureMinItem> temperatureMin;

	@SerializedName("temperature_max")
	private List<TemperatureMaxItem> temperatureMax;

	@SerializedName("temperature")
	private List<TemperatureItem> temperature;

	@SerializedName("weather")
	private List<WeatherItem> weather;

	@SerializedName("humidity")
	private List<HumidityItem> humidity;

	@SerializedName("wind_direction")
	private List<WindDirectionItem> windDirection;

	@SerializedName("wind_speed")
	private List<WindSpeedItem> windSpeed;

	@SerializedName("humidity_max")
	private List<HumidityMaxItem> humidityMax;

	public void setUpdate(String update){
		this.update = update;
	}

	public String getUpdate(){
		return update;
	}

	public void setKabupaten(String kabupaten){
		this.kabupaten = kabupaten;
	}

	public String getKabupaten(){
		return kabupaten;
	}

	public void setHumidityMin(List<HumidityMinItem> humidityMin){
		this.humidityMin = humidityMin;
	}

	public List<HumidityMinItem> getHumidityMin(){
		return humidityMin;
	}

	public void setTemperatureMin(List<TemperatureMinItem> temperatureMin){
		this.temperatureMin = temperatureMin;
	}

	public List<TemperatureMinItem> getTemperatureMin(){
		return temperatureMin;
	}

	public void setTemperatureMax(List<TemperatureMaxItem> temperatureMax){
		this.temperatureMax = temperatureMax;
	}

	public List<TemperatureMaxItem> getTemperatureMax(){
		return temperatureMax;
	}

	public void setTemperature(List<TemperatureItem> temperature){
		this.temperature = temperature;
	}

	public List<TemperatureItem> getTemperature(){
		return temperature;
	}

	public void setWeather(List<WeatherItem> weather){
		this.weather = weather;
	}

	public List<WeatherItem> getWeather(){
		return weather;
	}

	public void setHumidity(List<HumidityItem> humidity){
		this.humidity = humidity;
	}

	public List<HumidityItem> getHumidity(){
		return humidity;
	}

	public void setWindDirection(List<WindDirectionItem> windDirection){
		this.windDirection = windDirection;
	}

	public List<WindDirectionItem> getWindDirection(){
		return windDirection;
	}

	public void setWindSpeed(List<WindSpeedItem> windSpeed){
		this.windSpeed = windSpeed;
	}

	public List<WindSpeedItem> getWindSpeed(){
		return windSpeed;
	}

	public void setHumidityMax(List<HumidityMaxItem> humidityMax){
		this.humidityMax = humidityMax;
	}

	public List<HumidityMaxItem> getHumidityMax(){
		return humidityMax;
	}
}