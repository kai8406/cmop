package com.sobey.cmop.mvc.service.basicdata;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.dao.ServerModelDao;
import com.sobey.cmop.mvc.entity.ServerModel;
import com.sobey.framework.utils.DynamicSpecifications;
import com.sobey.framework.utils.SearchFilter;

/**
 * 服务器型号ServerModel 相关的管理类.
 * 
 * @author liukai
 * 
 */
@Service
@Transactional(readOnly = true)
public class ServerModelService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(ServerModelService.class);

	@Resource
	private ServerModelDao serverModelDao;

	public ServerModel getServerModel(Integer id) {
		return serverModelDao.findOne(id);
	}

	/**
	 * 根据服务器型号名称获得对应的ServerModel
	 * 
	 * @param name
	 * @return
	 */
	public ServerModel findServerModelByName(String name) {
		return serverModelDao.findByName(name);
	}

	/**
	 * 新增,保存ServerModel
	 * 
	 * @param serverModel
	 * @return
	 */
	@Transactional(readOnly = false)
	public ServerModel saveOrUpdate(ServerModel serverModel) {
		return serverModelDao.save(serverModel);
	}

	@Transactional(readOnly = false)
	public void delete(Integer id) {
		serverModelDao.delete(id);
	}

	/**
	 * 服务器型号页面(ServerModel)的分页查询.
	 * 
	 * @param searchParams
	 *            页面传递过来的参数
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<ServerModel> getServerModelPageable(Map<String, Object> searchParams, int pageNumber, int pageSize) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, new Sort(Direction.DESC, "id"));
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<ServerModel> spec = DynamicSpecifications.bySearchFilter(filters.values(), ServerModel.class);
		return serverModelDao.findAll(spec, pageRequest);
	}

	public List<ServerModel> getServerModelList() {
		return (List<ServerModel>) serverModelDao.findAll();
	}

	/**
	 * 新增服务器型号
	 * 
	 * @param company
	 *            所属公司alias+名称
	 * @param name
	 *            型号名称
	 * @param cpu
	 *            CPU个数
	 * @param memory
	 *            内存槽数
	 * @param disk
	 *            硬盘托架数
	 * @param pci
	 *            PCI插槽数
	 * @param port
	 *            网卡口数
	 */
	@Transactional(readOnly = false)
	public void saveServerModel(String company, String name, Integer cpu, Integer memory, Integer disk, Integer pci,
			Integer port) {

		String[] companys = StringUtils.split(company, "&");
		String companyAlias = companys[0];
		String companyName = companys[1];

		ServerModel serverModel = new ServerModel();
		serverModel.setCompany(companyName);
		serverModel.setCompanyAlias(companyAlias);
		serverModel.setName(name);
		serverModel.setCpu(cpu);
		serverModel.setMemory(memory);
		serverModel.setDisk(disk);
		serverModel.setPci(pci);
		serverModel.setPort(port);

		this.saveOrUpdate(serverModel);
	}

	/**
	 * 修改服务器型号
	 * 
	 * @param id
	 *            id
	 * @param company
	 *            所属公司alias+名称
	 * @param name
	 *            型号名称
	 * @param cpu
	 *            CPU个数
	 * @param memory
	 *            内存槽数
	 * @param disk
	 *            硬盘托架数
	 * @param pci
	 *            PCI插槽数
	 * @param port
	 *            网卡口数
	 */
	@Transactional(readOnly = false)
	public void updateServerModel(Integer id, String company, String name, Integer cpu, Integer memory, Integer disk,
			Integer pci, Integer port) {

		String[] companys = StringUtils.split(company, "&");
		String companyAlias = companys[0];
		String companyName = companys[1];

		ServerModel serverModel = this.getServerModel(id);
		serverModel.setCompany(companyName);
		serverModel.setCompanyAlias(companyAlias);
		serverModel.setName(name);
		serverModel.setCpu(cpu);
		serverModel.setMemory(memory);
		serverModel.setDisk(disk);
		serverModel.setPci(pci);
		serverModel.setPort(port);

		this.saveOrUpdate(serverModel);
	}

}
