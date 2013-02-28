package com.sobey.cmop.mvc.service.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.ws.BindingProvider;

public class HostTree {

	private static List<PropertyFilterSpec> createHostFilterSpec(ManagedObjectReference mObj) {
		PropertySpec pSpec = new PropertySpec();
		pSpec.setAll(Boolean.FALSE);
		pSpec.setType("VirtualMachine");
		pSpec.getPathSet().add("runtime");
		pSpec.getPathSet().add("config");
		pSpec.getPathSet().add("network");

		ObjectSpec oSpec = new ObjectSpec();
		oSpec.setObj(mObj);
		oSpec.setSkip(Boolean.FALSE);

		TraversalSpec tSpec = new TraversalSpec();
		tSpec.setType("HostSystem");
		tSpec.setPath("vm");
		tSpec.setSkip(Boolean.FALSE);

		oSpec.getSelectSet().add(tSpec);

		PropertyFilterSpec pfSpec = new PropertyFilterSpec();
		pfSpec.getObjectSet().add(oSpec);
		pfSpec.getPropSet().add(pSpec);

		List<PropertyFilterSpec> fSpecList = new ArrayList<PropertyFilterSpec>();
		fSpecList.add(pfSpec);
		return fSpecList;
	}

	private static List collectProperties(VimPortType methods, ServiceContent sContent) throws Exception {
		TraversalSpec computeResourceHostTraversalSpec = new TraversalSpec();
		computeResourceHostTraversalSpec.setName("computeResourceHostTraversalSpec");
		computeResourceHostTraversalSpec.setType("ComputeResource");
		computeResourceHostTraversalSpec.setPath("host");
		computeResourceHostTraversalSpec.setSkip(false);

		TraversalSpec datacenterHostTraversalSpec = new TraversalSpec();
		datacenterHostTraversalSpec.setName("datacenterHostTraversalSpec");
		datacenterHostTraversalSpec.setType("Datacenter");
		datacenterHostTraversalSpec.setPath("hostFolder");
		datacenterHostTraversalSpec.setSkip(false);
		SelectionSpec sSpecF = new SelectionSpec();
		sSpecF.setName("folderTraversalSpec");
		datacenterHostTraversalSpec.getSelectSet().add(sSpecF);

		TraversalSpec folderTraversalSpec = new TraversalSpec();
		folderTraversalSpec.setName("folderTraversalSpec");
		folderTraversalSpec.setType("Folder");
		folderTraversalSpec.setPath("childEntity");
		folderTraversalSpec.setSkip(false);
		folderTraversalSpec.getSelectSet().add(sSpecF);
		folderTraversalSpec.getSelectSet().add(datacenterHostTraversalSpec);
		folderTraversalSpec.getSelectSet().add(computeResourceHostTraversalSpec);

		ObjectSpec oSpec = new ObjectSpec();
		oSpec.setObj(sContent.getRootFolder());
		oSpec.setSkip(false);
		oSpec.getSelectSet().add(folderTraversalSpec);

		PropertySpec pSpec = new PropertySpec();
		pSpec.setType("HostSystem");
		pSpec.getPathSet().add("name");

		PropertyFilterSpec spec = new PropertyFilterSpec();
		spec.getPropSet().add(pSpec);
		spec.getObjectSet().add(oSpec);

		List<PropertyFilterSpec> fSpecList = new ArrayList<PropertyFilterSpec>();
		fSpecList.add(spec);

		RetrieveOptions ro = new RetrieveOptions();
		RetrieveResult hosts = methods.retrievePropertiesEx(sContent.getPropertyCollector(), fSpecList, ro);

		List<Map> hostListMap = new ArrayList<Map>();
		List<String> hostList = new ArrayList<String>();
		List<String> ecsList = null;
		if (hosts != null) {
			int countHost = 0;
			for (ObjectContent oc : hosts.getObjects()) {
				ManagedObjectReference mor = oc.getObj();
				String host = null;
				List<DynamicProperty> dps = oc.getPropSet();
				if (dps != null) {
					for (DynamicProperty dp : dps) {
						host = (String) dp.getVal();
						hostList.add(host);
					}

					try {
						List<PropertyFilterSpec> hostFilterSpec = createHostFilterSpec(mor);
						RetrieveResult vms = methods.retrievePropertiesEx(sContent.getPropertyCollector(), hostFilterSpec, ro);

						VirtualMachineConfigInfo vmConfig;
						VirtualMachineRuntimeInfo vmRuntime;
						VirtualHardware vHardware;
						List<VirtualDevice> vDevice;
						VirtualDisk vDisk = null;
						int countVm = 0;
						String ecs;
						if (vms != null) {
							ecsList = new ArrayList<String>();
							for (ObjectContent ocHost : vms.getObjects()) {
								List<DynamicProperty> dpsHost = ocHost.getPropSet();
								if (dpsHost != null) {
									for (DynamicProperty dp : dpsHost) {
										if ("config".equals(dp.getName())) {
											vmConfig = (VirtualMachineConfigInfo) dp.getVal();
											vHardware = vmConfig.getHardware();
											vDevice = vHardware.getDevice();
											for (int i = 0; i < vDevice.size(); i++) {
												if (vDevice.get(i) instanceof VirtualDisk) {
													vDisk = (VirtualDisk) vDevice.get(i);
													break;
												}
											}
											ecs = vmConfig.getName();
											System.out.print(ecs);
											if (ecs.length() > 0 && ecs.indexOf("_") > 0) {
												ecs = ecs.substring(0, ecs.indexOf("_"));
											}
											if (ecs.length() > 0 && ecs.length() <= 15 && ecs.split("\\.").length == 4 && ecs.replaceAll("\\.", "").matches("[0-9]+")) {
												ecsList.add(ecs);
												System.out.println("-->" + ecs);
											} else {
												System.out.println();
											}
										}
									}
								}
								countVm++;
							}
						} else {
							ecsList = new ArrayList<String>();
						}
						Map hostMap = new HashMap();
						hostMap.put(host, ecsList);
						hostListMap.add(hostMap);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				countHost++;
			}
		} else {
			System.out.println("No Managed Entities retrieved!");
		}
		List list = new ArrayList();
		list.add(hostList);
		list.add(hostListMap);
		return list;
	}

	private static class TrustAllTrustManager implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
			return;
		}
	}

	public static List call() throws Exception {
		String url = "https://172.20.0.20/sdk/vimService";
		ManagedObjectReference SVC_INST_REF = new ManagedObjectReference();
		VimService vimService;
		VimPortType vimPort;
		ServiceContent serviceContent;
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return true;
			}
		};
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new TrustAllTrustManager();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
		javax.net.ssl.SSLSessionContext sslsc = sc.getServerSessionContext();
		sslsc.setSessionTimeout(0);
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		SVC_INST_REF.setType("ServiceInstance");
		SVC_INST_REF.setValue("ServiceInstance");
		vimService = new VimService();
		vimPort = vimService.getVimPort();
		Map<String, Object> ctxt = ((BindingProvider) vimPort).getRequestContext();
		ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
		ctxt.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);
		serviceContent = vimPort.retrieveServiceContent(SVC_INST_REF);
		vimPort.login(serviceContent.getSessionManager(), "administrator", "Newmed!ad3v@s0bey", null);
		List list = collectProperties(vimPort, serviceContent);
		vimPort.logout(serviceContent.getSessionManager());
		return list;
	}

	public static void main(String[] args) throws Exception {
		String serverName = args[0];
		String userName = args[1];
		String password = args[2];
		String url = "https://" + serverName + "/sdk/vimService";
		ManagedObjectReference SVC_INST_REF = new ManagedObjectReference();
		VimService vimService;
		VimPortType vimPort;
		ServiceContent serviceContent;
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return true;
			}
		};
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new TrustAllTrustManager();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
		javax.net.ssl.SSLSessionContext sslsc = sc.getServerSessionContext();
		sslsc.setSessionTimeout(0);
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		SVC_INST_REF.setType("ServiceInstance");
		SVC_INST_REF.setValue("ServiceInstance");
		vimService = new VimService();
		vimPort = vimService.getVimPort();
		Map<String, Object> ctxt = ((BindingProvider) vimPort).getRequestContext();
		ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
		ctxt.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);
		serviceContent = vimPort.retrieveServiceContent(SVC_INST_REF);
		vimPort.login(serviceContent.getSessionManager(), userName, password, null);
		collectProperties(vimPort, serviceContent);
		vimPort.logout(serviceContent.getSessionManager());
	}
}
