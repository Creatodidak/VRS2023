package id.creatodidak.vrspolreslandak.auth;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ModelSatfung{

	@SerializedName("satfung")
	private List<SatfungItem> satfung;

	public void setSatfung(List<SatfungItem> satfung){
		this.satfung = satfung;
	}

	public List<SatfungItem> getSatfung(){
		return satfung;
	}
}