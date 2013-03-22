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
import com.sobey.framework.utils.StringCommonUtils;

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
				mdnVodItem.setVodProtocol(StringCommonUtils.replaceAndSubstringText((vodProtocols[i]), "-", ","));
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
				mdnLiveItem.setLiveProtocol(StringCommonUtils.replaceAndSubstringText((liveProtocols[i]), "-", ","));
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

	/**
	 * 更新mdn live (apply)
	 * 
	 * @param mdnLiveItem
	 * @param liveDomain
	 *            直播服务域名
	 * @param liveBandwidth
	 *            直播加速服务带宽（含单位）
	 * @param liveProtocol
	 *            播放协议数组
	 * @param bandwidth
	 *            出口带宽（含单位）
	 * @param name
	 *            频道名称
	 * @param guid
	 *            频道GUID
	 * @param streamOutMode
	 *            直播流输出模式：1-Encoder模式；2-Transfer模式
	 * @param encoderMode
	 *            编码器模式：1-HTTP拉流；2-RTMP推流
	 * @param httpUrlEncoder
	 *            HTTP拉流编码模式下的HTTP流地址
	 * @param httpBitrate
	 *            HTTP拉流编码模式下的HTTP流混合码率
	 * @param hlsUrlEncoder
	 *            RTMP推流编码模式下的M3U8流地址
	 * @param hlsBitrateEncoder
	 *            RTMP推流编码模式下的M3U8流混合码率
	 * @param httpUrl
	 *            HTTP流地址
	 * @param httpBitrateEncoder
	 *            HTTP流混合码率
	 * @param hlsUrl
	 *            M3U8流地址
	 * @param hlsBitrate
	 *            M3U8流混合码率
	 * @param rtspUrl
	 *            RTSP流地址
	 * @param rtspBitrate
	 *            RTSP流混合码率
	 */
	@Transactional(readOnly = false)
	public void updateMdnLiveItemToApply(MdnLiveItem mdnLiveItem, String bandwidth, String name, String guid, String liveDomain, String liveBandwidth, String liveProtocol, Integer streamOutMode,
			Integer encoderMode, String httpUrlEncoder, String httpBitrateEncoder, String hlsUrlEncoder, String hlsBitrateEncoder, String httpUrl, String httpBitrate, String hlsUrl,
			String hlsBitrate, String rtspUrl, String rtspBitrate) {

		mdnLiveItem.setBandwidth(bandwidth);
		mdnLiveItem.setName(name);
		mdnLiveItem.setGuid(guid);
		mdnLiveItem.setLiveDomain(liveDomain);
		mdnLiveItem.setLiveBandwidth(liveBandwidth);
		mdnLiveItem.setLiveProtocol(liveProtocol);
		mdnLiveItem.setStreamOutMode(streamOutMode);
		mdnLiveItem.setEncoderMode(encoderMode);

		if (MdnConstant.OutputMode.Encoder模式.toInteger().equals(streamOutMode)) {

			if (MdnConstant.EncoderMode.HTTP拉流模式.toInteger().equals(encoderMode)) {

				mdnLiveItem.setHttpBitrate(httpUrlEncoder);
				mdnLiveItem.setHttpUrl(httpBitrateEncoder);

				mdnLiveItem.setHlsBitrate(null);
				mdnLiveItem.setHlsUrl(null);

			} else {

				mdnLiveItem.setHttpBitrate(null);
				mdnLiveItem.setHttpUrl(null);

				mdnLiveItem.setHlsBitrate(hlsUrlEncoder);
				mdnLiveItem.setHlsUrl(hlsBitrateEncoder);

			}

			mdnLiveItem.setRtspBitrate(null);
			mdnLiveItem.setRtspUrl(null);
		} else {

			mdnLiveItem.setHttpBitrate(httpBitrate);
			mdnLiveItem.setHttpUrl(httpUrl);

			mdnLiveItem.setHlsBitrate(hlsBitrate);
			mdnLiveItem.setHlsUrl(hlsUrl);

			mdnLiveItem.setRtspBitrate(rtspBitrate);
			mdnLiveItem.setRtspUrl(rtspUrl);

		}

		comm.mdnService.saveOrUpdate(mdnLiveItem);

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