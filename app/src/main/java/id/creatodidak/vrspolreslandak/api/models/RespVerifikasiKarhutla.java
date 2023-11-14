package id.creatodidak.vrspolreslandak.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespVerifikasiKarhutla{

	@SerializedName("dataverifikasikarhutla")
	private List<DataverifikasikarhutlaItem> dataverifikasikarhutla;

	public void setDataverifikasikarhutla(List<DataverifikasikarhutlaItem> dataverifikasikarhutla){
		this.dataverifikasikarhutla = dataverifikasikarhutla;
	}

	public List<DataverifikasikarhutlaItem> getDataverifikasikarhutla(){
		return dataverifikasikarhutla;
	}
}