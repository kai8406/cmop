package com.sobey.cmop.mvc.service.paas;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.constant.MdnConstant;
import com.sobey.cmop.mvc.constant.ResourcesConstant;
import com.sobey.cmop.mvc.dao.MdnItemDao;
import com.sobey.cmop.mvc.dao.MdnLiveItemDao;
import com.sobey.cmop.mvc.dao.MdnVodItemDao;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.MdnItem;
import com.sobey.cmop.mvc.entity.MdnLiveItem;
import com.sobey.cmop.mvc.entity.MdnVodItem;

/**
 * MDN相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class MdnService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(MdnService.class);

	@Resource
	private MdnItemDao mdnItemDao;

	@Resource
	private MdnLiveItemDao mdnLiveItemDao;

	@Resource
	private MdnVodItemDao mdnVodItemDao;

	/* ============== MDN ============== */

	public MdnItem getMdnItem(Integer id) {
		return mdnItemDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public MdnItem saveOrUpdate(MdnItem mdnItem) {
		return mdnItemDao.save(mdnItem);
	}

	@Transactional(readOnly = false)
	public void deleteMdnItem(Integer id) {
		mdnItemDao.delete(id);
	}

	/**
	 * 
	 * @param apply
	 *            服务申请
	 * @param coverArea
	 *            覆盖地区
	 * @param coverIsp
	 *            覆盖ISP
	 * @param vodDomains
	 *            点播域名数组
	 * @param vodBandwidths
	 *            点播加速服务带宽（含单位）数组
	 * @param vodProtocols
	 *            播放协议数组
	 * @param sourceOutBandwidths
	 *            源站出口带宽（含单位）数组
	 * @param sourceStreamerUrls
	 *            源站Streamer公网地址 数组
	 * @param liveDomains
	 *            直播服务域名数组
	 * @param liveBandwidths
	 *            直播加速服务带宽（含单位）数组
	 * @param liveProtocols
	 *            播放协议数组
	 * @param bandwidths
	 *            出口带宽（含单位）数组
	 * @param channelNames
	 *            频道名称数组
	 * @param channelGUIDs
	 *            频道GUID数组
	 * @param streamOutModes
	 *            直播流输出模式：1-Encoder模式；2-Transfer模式 数组
	 * @param encoderModes
	 *            编码器模式：1-HTTP拉流；2-RTMP推流 数组
	 * @param httpUrls
	 *            HTTP流地址 数组
	 * @param httpBitrates
	 *            HTTP流混合码率数组
	 * @param hlsUrls
	 *            M3U8流地址数组
	 * @param hlsBitrates
	 *            M3U8流混合码率数组
	 * @param rtspUrls
	 *            RTSP流地址数组
	 * @param rtspBitrates
	 *            RTSP流混合码率数组
	 */
	@Transactional(readOnly = false)
	public void saveMdnToApply(Apply apply, String coverArea, String coverIsp,
			// vod
			String[] vodDomains, String[] vodBandwidths, String[] vodProtocols, String[] sourceOutBandwidths,
			String[] sourceStreamerUrls
			// live
			, String[] liveDomains, String[] liveBandwidths, String[] liveProtocols, String[] bandwidths, String[] channelNames, String[] channelGUIDs, String[] streamOutModes, String[] encoderModes,
			String[] httpUrls, String[] httpBitrates, String[] hlsUrls, String[] hlsBitrates, String[] rtspUrls, String[] rtspBitrates) {

		// Step1. 创建一个服务申请Apply

		comm.applyService.saveApplyByServiceType(apply, ApplyConstant.ServiceType.MDN.toInteger());

		// Step2. 创建一个MDN.

		MdnItem mdnItem = new MdnItem();
		mdnItem.setApply(apply);
		mdnItem.setCoverArea(coverArea);
		mdnItem.setCoverIsp(coverIsp);
		mdnItem.setIdentifier(comm.applyService.generateIdentifier(ResourcesConstant.ServiceType.MDN.toInteger()));
		this.saveOrUpdate(mdnItem);

		// Step2. 创建vod MDN.

		this.saveMdnVodItem(mdnItem, vodDomains, vodBandwidths, vodProtocols, sourceOutBandwidths, sourceStreamerUrls);

		// Step3. 创建live MDN.

		this.saveMdnLiveItem(mdnItem, liveDomains, liveBandwidths, liveProtocols, bandwidths, channelNames, channelGUIDs, streamOutModes, encoderModes, httpUrls, httpBitrates, hlsUrls, hlsBitrates,
				rtspUrls, rtspBitrates);

	}

	/* ============== Vod ============== */

	/**
	 * 新增多个点播MdnVodItem
	 * 
	 * @param mdnItem
	 *            mdn对象
	 * @param vodDomains
	 *            点播域名数组
	 * @param vodBandwidths
	 *            点播加速服务带宽（含单位）数组
	 * @param vodProtocols
	 *            播放协议数组
	 * @param sourceOutBandwidths
	 *            源站出口带宽（含单位）数组
	 * @param sourceStreamerUrls
	 *            源站Streamer公网地址 数组
	 */
	@Transactional(readOnly = false)
	public void saveMdnVodItem(MdnItem mdnItem, String[] vodDomains, String[] vodBandwidths, String[] vodProtocols, String[] sourceOutBandwidths, String[] sourceStreamerUrls) {

		if (vodDomains != null) {
			for (int i = 0; i < vodDomains.length; i++) {
				MdnVodItem mdnVodItem = new MdnVodItem();
				mdnVodItem.setMdnItem(mdnItem);
				mdnVodItem.setSourceOutBandwidth(sourceOutBandwidths[i]);
				mdnVodItem.setSourceStreamerUrl(sourceStreamerUrls[i]);
				mdnVodItem.setVodBandwidth(vodBandwidths[i]);
				mdnVodItem.setVodDomain(vodDomains[i]);
				mdnVodItem.setVodProtocol(vodProtocols[i]);
				this.saveOrUpdate(mdnVodItem);
			}
		}
	}

	public MdnVodItem getMdnVodItem(Integer id) {
		return mdnVodItemDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public MdnVodItem saveOrUpdate(MdnVodItem mdnVodItem) {
		return mdnVodItemDao.save(mdnVodItem);
	}

	@Transactional(readOnly = false)
	public void deleteMdnVodItem(Integer id) {
		mdnVodItemDao.delete(id);
	}

	/**
	 * 获得指定MDN下的所有点播
	 * 
	 * @param mdnId
	 * @return
	 */
	public List<MdnVodItem> getMdnVodItemByMdnId(Integer mdnId) {
		return mdnVodItemDao.findByMdnItemId(mdnId);
	}

	/* ============== Live ============== */

	/**
	 * 新增多个直播MdnLiveItem
	 * 
	 * @param mdnItem
	 *            mdn对象
	 * @param liveDomains
	 *            直播服务域名数组
	 * @param liveBandwidths
	 *            直播加速服务带宽（含单位）数组
	 * @param liveProtocols
	 *            播放协议数组
	 * @param bandwidths
	 *            出口带宽（含单位）数组
	 * @param channelNames
	 *            频道名称数组
	 * @param channelGUIDs
	 *            频道GUID数组
	 * @param streamOutModes
	 *            直播流输出模式：1-Encoder模式；2-Transfer模式数组
	 * @param encoderModes
	 *            编码器模式：1-HTTP拉流；2-RTMP推流数组
	 * @param httpUrls
	 *            HTTP流地址数组
	 * @param httpBitrates
	 *            HTTP流混合码率数组
	 * @param hlsUrls
	 *            M3U8流地址数组
	 * @param hlsBitrates
	 *            M3U8流混合码率数组
	 * @param rtspUrls
	 *            RTSP流地址数组
	 * @param rtspBitrates
	 *            RTSP流混合码率数组
	 */
	@Transactional(readOnly = false)
	public void saveMdnLiveItem(MdnItem mdnItem, String[] liveDomains, String[] liveBandwidths, String[] liveProtocols, String[] bandwidths, String[] channelNames, String[] channelGUIDs,
			String[] streamOutModes, String[] encoderModes, String[] httpUrls, String[] httpBitrates, String[] hlsUrls, String[] hlsBitrates, String[] rtspUrls, String[] rtspBitrates) {

		if (liveDomains != null) {
			for (int i = 0; i < liveDomains.length; i++) {
				MdnLiveItem mdnLiveItem = new MdnLiveItem();

				Integer encoderMode = Integer.valueOf(encoderModes[i]);
				Integer streamOutMode = Integer.valueOf(streamOutModes[i]);

				mdnLiveItem.setMdnItem(mdnItem);
				mdnLiveItem.setLiveDomain(liveDomains[i]);
				mdnLiveItem.setLiveBandwidth(liveBandwidths[i]);
				mdnLiveItem.setLiveProtocol(liveProtocols[i]);
				mdnLiveItem.setStreamOutMode(streamOutMode);
				mdnLiveItem.setName(channelNames[i]);
				mdnLiveItem.setGuid(channelGUIDs[i]);
				mdnLiveItem.setBandwidth(bandwidths[i]);
				mdnLiveItem.setEncoderMode(encoderMode);

				if (MdnConstant.OutputMode.Encoder模式.toInteger().equals(streamOutMode)) {

					if (MdnConstant.EncoderMode.HTTP拉流模式.toInteger().equals(encoderMode)) {

						mdnLiveItem.setHttpBitrate(httpBitrates[i]);
						mdnLiveItem.setHttpUrl(httpUrls[i]);

						mdnLiveItem.setHlsBitrate(null);
						mdnLiveItem.setHlsUrl(null);

					} else {

						mdnLiveItem.setHttpBitrate(null);
						mdnLiveItem.setHttpUrl(null);

						mdnLiveItem.setHlsBitrate(hlsBitrates[i]);
						mdnLiveItem.setHlsUrl(hlsUrls[i]);

					}

					mdnLiveItem.setRtspBitrate(null);
					mdnLiveItem.setRtspUrl(null);
				} else {

					mdnLiveItem.setHttpBitrate(httpBitrates[i]);
					mdnLiveItem.setHttpUrl(httpUrls[i]);

					mdnLiveItem.setHlsBitrate(hlsBitrates[i]);
					mdnLiveItem.setHlsUrl(hlsUrls[i]);

					mdnLiveItem.setRtspBitrate(rtspBitrates[i]);
					mdnLiveItem.setRtspUrl(rtspUrls[i]);

				}

				this.saveOrUpdate(mdnLiveItem);
			}
		}

	}

	public MdnLiveItem getMdnLiveItem(Integer id) {
		return mdnLiveItemDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public MdnLiveItem saveOrUpdate(MdnLiveItem mdnLiveItem) {
		return mdnLiveItemDao.save(mdnLiveItem);
	}

	@Transactional(readOnly = false)
	public void deleteMdnLiveItem(Integer id) {
		mdnLiveItemDao.delete(id);
	}

	/**
	 * 获得指定mdn下的所有直播
	 * 
	 * @param mdnId
	 * @return
	 */
	public List<MdnLiveItem> getMdnLiveItemByMdnId(Integer mdnId) {
		return mdnLiveItemDao.findByMdnItemId(mdnId);
	}

}
