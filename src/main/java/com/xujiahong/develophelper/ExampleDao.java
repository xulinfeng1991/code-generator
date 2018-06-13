package com.xujiahong.develophelper;

import com.xujiahong.develophelper.Example;
import java.util.List;

/**
 * Created by AutoGenerateCode on 2017-08-14 14:58:47.
 */  
public interface ExampleDao {
	
	//====================【增】====================
	
	int insert(Example obj);
	
	//====================【删】====================
	
	int delete(Long exampleId);
	
	//====================【改】====================
	
	int update(Example obj);
	
	//====================【查】====================
	
	Example detail(Long exampleId);
	public List<Example> list(Example obj);

}