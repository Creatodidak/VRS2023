package id.creatodidak.vrspolreslandak.api.models.karhutla;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ListVerKampanye{

	@SerializedName("listverkampanye")
	private List<ListverkampanyeItem> listverkampanye;

	public void setListverkampanye(List<ListverkampanyeItem> listverkampanye){
		this.listverkampanye = listverkampanye;
	}

	public List<ListverkampanyeItem> getListverkampanye(){
		return listverkampanye;
	}
}