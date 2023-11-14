package id.creatodidak.vrspolreslandak.api.models.cuaca;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataCuaca{

	@SerializedName("datacuaca")
	private List<DatacuacaItem> datacuaca;

	@SerializedName("status")
	private boolean status;

	public void setDatacuaca(List<DatacuacaItem> datacuaca){
		this.datacuaca = datacuaca;
	}

	public List<DatacuacaItem> getDatacuaca(){
		return datacuaca;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}
}