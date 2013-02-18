package com.sobey.cmop.mvc.service.basicdata.imports;

import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;

@Service
@Transactional(readOnly = true)
public class ImportService extends BaseSevcie {

	public boolean save(InputStream inputStream) {
		// TODO Auto-generated method stub
		return false;
	}

}