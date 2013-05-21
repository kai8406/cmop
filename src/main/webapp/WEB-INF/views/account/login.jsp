<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

<title>主页</title>

<script>
	$(document).ready(function() {

		//针对navbar.jsp中的登录form.

		$("#username").focus();

	});
</script>

</head>
<body>

	<div class="hero-unit">
		<h1>全媒体云平台管理系统－CMOP</h1>
	</div>

	<div class="row">
		<section class="span4">
			<h2>PCS</h2>
			<ul>
				<li>在某些场景里，您可能需要继续使用物理服务器或者其他的物理设备，这些物理设备需要云主机之间进行通信。我们支持物理设备与云主机之间的组网。</li>
				<li>多种主机和镜像选择。按照不同的业务场景，提供了多款不同计算能力的云主机。云主机支持多种操作系统，包括Windows，Linux，CentOS等。
				</li>
			</ul>
		</section>

		<section class="span4">
			<h2>ECS</h2>
			<ul>
				<li>弹性计算云可以在几分钟内增加、扩展或者缩减您的计算能力，更改硬件配置和镜像。</li>
				<li>相比传统运维模式，弹性计算云可以节省40%以上带宽成本，降低综合运维成本约50%，缩短新应用上线时间超过90%，大幅降低用户的CAPEX。</li>
				<li>多种主机和镜像选择。按照不同的应用特点，提供了多款不同计算能力的云主机。云主机支持多种操作系统，包括Windows，Linux，CentOS等。
				</li>
			</ul>
		</section>

		<section class="span4">
			<h2>ES3</h2>
			<ul>
				<li>安全稳定，无限扩容。可以附加多个云硬盘以提高云硬盘的性能。</li>
				<li>吞吐稳定且一致性。ES3在最大程度上保证在访问波动较大情况，让吞吐量性能表现稳定和一致性。</li>
			</ul>
		</section>
	</div>

	<div class="row">
		<section class="span4">
			<h2>ELB</h2>
			<ul>
				<li>通过设置虚拟服务IP，将位于同一机房的多台云服务器资源虚拟成一个高性能、高可用的应用服务池。</li>
				<li>根据应用特性，将来自客户端的网络请求分发到云服务池中。</li>
			</ul>
		</section>

		<section class="span4">
			<h2>EIP</h2>
			<ul>
				<li>公网IP地址服务。在CMOP中，IP是独立产品模块，用户可对已购的IP进行绑定、解绑云主机、释放等操作。</li>
				<li>内网IP地址服务。当你新增一个安全组后，将会分配给你一段连续的内网IP段，新增进入的云主机以DHCP的方式分配一个内网IP。</li>
			</ul>
		</section>

		<section class="span4">
			<h2>DNS</h2>
			<ul>
				<li>实现分布服务负载均衡、冗余度和提高分布式安全的同时，根据用户所在的ISP、地域进行不同的智能解析策略配置，为用户返回最优服务节点（响应速度最快节点、本地节点）。</li>
				<li>多个域名类型选择。可选择GSLB、A、CNAME映射不同的IP和CNAME域名。</li>
			</ul>
		</section>
	</div>

	<div class="row">
		<section class="span4">
			<h2>ESG</h2>
			<ul>
				<li>一个云主机安全组（VM Security Group）是一组云主机的集合，也是关于这组云主机的网络安全规则的集合。</li>
				<li>每个云主机安全组包括一组云主机实例、一组网络安全规则。</li>
				<li>用户可以基于云主机安全组批量定义云主机的网络安全策略。</li>
			</ul>
		</section>

		<section class="span4">
			<h2>实例监控</h2>
			<ul>
				<li>您可以指定一个实例对其监控，根据监测结果，动态增加或减少实例的数目。</li>
				<li>有众多的监控属性。包含CPU占用率、内存占用率、网络丢包率、硬盘可用率、网络延时率、最大进程数、监控端口、监控进程、最大进程。</li>
				<li>可将监控结果发送至指定的邮件和手机列表。</li>
			</ul>
		</section>

		<section class="span4">
			<h2>ELB监控</h2>
			<ul>
				<li>您可以指定一个ELB对其监控，根据监测结果，动态增加或减少弹性负载均衡器的数目。</li>
				<li>可将监控结果发送至指定的邮件和手机列表。</li>
			</ul>
		</section>
	</div>

</body>
</html>