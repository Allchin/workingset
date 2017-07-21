package cn.allchin.jvm.param;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hp.hpl.jena.util.FileUtils;

/**
 * 解析  启动jvm参数
 * @author renxing.zhang
 *
 */
/**
 * @author renxing.zhang
 *
 */
public class JvmStartUpParamParser {
	public static Map<String,JvmParamDesc> paramMap=new HashMap();
	static{
 
		paramMap.put("-Xms",new JvmParamDesc( "初始堆内存"));
		paramMap.put("-Xmx",new JvmParamDesc( "最大堆内存"));
		
		
		paramMap.put("-server",new JvmParamDesc( "服务器模式"));
		paramMap.put("-XX:+DisableExplicitGC",new JvmParamDesc( "禁止在运行期显式地调用System.gc()"));
		paramMap.put("-verbose:gc",new JvmParamDesc( "输出虚拟机中GC的详细情况[Full GC 168K->97K(1984K)， 0.0253873 secs]"));

		paramMap.put("-Xloggc",new JvmParamDesc( "gc日志产生的路径"));
		
		paramMap.put("-Djava.endorsed.dirs",new JvmParamDesc( "指定的目录面放置的jar文件，将有覆盖系统API的功能"));
		paramMap.put("-classpath",new JvmParamDesc( "类路径"));
		
		paramMap.put("-Xss",new JvmParamDesc( "线程栈大小"));
		paramMap.put("-XX:PermSize",new JvmParamDesc( "永久带初始值"));
		
		paramMap.put("-XX:MaxDirectMemorySize",new JvmParamDesc( "直接内存最大值"));
		paramMap.put("-cp",new JvmParamDesc( ""));
		paramMap.put("-n",new JvmParamDesc( ""));
		paramMap.put("-XX:NewRatio",new JvmParamDesc( "新生代和年老代的堆内存占用比例, 例如2表示新生代占年老代的1/2"));
		paramMap.put("-XX:NewSize",new JvmParamDesc( "新生代占整个堆内存的最大值"));
		 
		paramMap.put("-XX:-AllowUserSignalHandlers",new JvmParamDesc( "允许为java进程安装信号处理器,信号处理参见类:sun.misc.Signal, sun.misc.SignalHandler"));
		paramMap.put("-XX:+FailOverToOldVerifier",new JvmParamDesc( "如果新的Class校验器检查失败，则使用老的校验器(失败原因:因为JDK6最高向下兼容到JDK1.2，而JDK1.2的class info 与JDK6的info存在较大的差异，所以新校验器可能会出现校验失败的情况)"));
		paramMap.put("-XX:+HandlePromotionFailure",new JvmParamDesc( "关闭新生代收集担保"));
		paramMap.put("-XX:+MaxFDLimit",new JvmParamDesc( "设置java进程可用文件描述符为操作系统允许的最大值。"));
		paramMap.put("-XX:PreBlockSpin",new JvmParamDesc( "控制多线程自旋锁优化的自旋次数"));
		paramMap.put("-XX:-RelaxAccessControlCheck",new JvmParamDesc( "在Class校验器中，放松对访问控制的检查,作用与reflection里的setAccessible类似"));
		paramMap.put("-XX:+ScavengeBeforeFullGC",new JvmParamDesc( "	在Full GC前触发一次Minor GC"));
		paramMap.put("-XX:+UseAltSigs",new JvmParamDesc( "为了防止与其他发送信号的应用程序冲突，允许使用候补信号替代 SIGUSR1和SIGUSR2"));
		paramMap.put("-XX:+UseBoundThreads",new JvmParamDesc( "绑定所有的用户线程到内核线程, 减少线程进入饥饿状态（得不到任何cpu time）的次数"));
		
		paramMap.put("-XX:-UseConcMarkSweepGC",new JvmParamDesc( "启用CMS低停顿垃圾收集器,减少FGC的暂停时间"));
		paramMap.put("-XX:+UseGCOverheadLimit",new JvmParamDesc(true, "限制GC的运行时间。如果GC耗时过长，就抛OOM"));
		
		paramMap.put("-XX:+UseLWPSynchronization",new JvmParamDesc( "使用轻量级进程（内核线程）替换线程同步"));
		paramMap.put("-XX:-UseParallelGC",new JvmParamDesc( "策略为新生代使用并行清除，年老代使用单线程Mark-Sweep-Compact的垃圾收集器"));
		paramMap.put("-XX:-UseParallelOldGC",new JvmParamDesc( "策略为老年代和新生代都使用并行清除的垃圾收集器"));
		paramMap.put("-XX:-UseSerialGC",new JvmParamDesc("使用串行垃圾收集器"));
		paramMap.put("-XX:-UseSpinning",new JvmParamDesc(16,true, "启用多线程自旋锁优化"));
		paramMap.put("-XX:+UseTLAB",new JvmParamDesc( "启用线程本地缓存区"));
		paramMap.put("-XX:+UseSplitVerifier",new JvmParamDesc( "使用新的Class类型校验器"));
		paramMap.put("-XX:+UseThreadPriorities",new JvmParamDesc( 16,true,"使用本地线程的优先级"));
		
		paramMap.put("-XX:+UseVMInterruptibleIO",new JvmParamDesc( "在solaris中，允许运行时中断线程"));
		//性能
		paramMap.put("-XX:+AggressiveOpts",new JvmParamDesc( "启用JVM开发团队最新的调优成果。例如编译优化，偏向锁，并行年老代收集等"));
		paramMap.put("-XX:CompileThreshold",new JvmParamDesc( "通过JIT编译器，将方法编译成机器码的触发阀值，可以理解为调用方法的次数，例如调1000次，将方法编译为机器码"));
		paramMap.put("-XX:LargePageSizeInBytes",new JvmParamDesc( "设置堆内存的内存页大小"));
		
		paramMap.put("-XX:MaxHeapFreeRatio",new JvmParamDesc( "GC后，如果发现空闲堆内存占到整个预估上限值的70%，则收缩预估上限值"));
		paramMap.put("-XX:MaxNewSize",new JvmParamDesc( "新生代占整个堆内存的最大值"));
		paramMap.put("-XX:MaxPermSize",new JvmParamDesc( "Perm（俗称方法区）占整个堆内存的最大值"));
		paramMap.put("-XX:MinHeapFreeRatio",new JvmParamDesc( "GC后，如果发现空闲堆内存占到整个预估上限值的40%，则增大上限值"));
		
		
		paramMap.put("-XX:ReservedCodeCacheSize",new JvmParamDesc( "设置代码缓存的最大值，编译时用"));
		paramMap.put("-XX:SurvivorRatio",new JvmParamDesc( "Eden与Survivor的占用比例。例如8表示，一个survivor区占用 1/8 的Eden内存，即1/10的新生代内存，为什么不是1/9？因为我们的新生代有2个survivor，即S0和S1。所以survivor总共是占用新生代内存的 2/10，Eden与新生代的占比则为 8/10"));
		paramMap.put("-XX:TargetSurvivorRatio",new JvmParamDesc( "实际使用的survivor空间大小占比。默认是50%，最高90%"));
		paramMap.put("-XX:ThreadStackSize",new JvmParamDesc( "线程堆栈大小"));
		paramMap.put("-XX:+UseBiasedLocking",new JvmParamDesc( "启用偏向锁"));
		
		paramMap.put("-XX:+UseFastAccessorMethods",new JvmParamDesc( "优化原始类型的getter方法性能(get/set:Primitive Type)"));
		paramMap.put("-XX:-UseISM",new JvmParamDesc( "启用solaris的ISM"));
		paramMap.put("-XX:+UseLargePages",new JvmParamDesc( "启用大内存分页"));
		paramMap.put("-XX:+UseMPSS",new JvmParamDesc( "启用solaris的MPSS，不能与ISM同时使用"));
		paramMap.put("-XX:+UseStringCache",new JvmParamDesc(true, "启用缓存常用的字符串。"));
		
		paramMap.put("-XX:AllocatePrefetchLines",new JvmParamDesc( "Number of cache lines to load after the last object allocation using prefetch instructions generated in JIT compiled code. Default values are 1 if the last allocated object was an instance and 3 if it was an array."));
		paramMap.put("-XX:AllocatePrefetchStyle",new JvmParamDesc( "Generated code style for prefetch instructions.0 – no prefetch instructions are generate*d*,1 – execute prefetch instructions after each allocation,2 – use TLAB allocation watermark pointer to gate when prefetch instructions are executed."));
		paramMap.put("-XX:+UseCompressedStrings",new JvmParamDesc( "其中，对于不需要16位字符的字符串，可以使用byte[] 而非char[]。对于许多应用，这可以节省内存，但速度较慢（5％-10％）"));
		paramMap.put("-XX:+OptimizeStringConcat",new JvmParamDesc( "优化字符串连接操作在可能的情况下"));
		
		//调试
		paramMap.put("-XX:-CITime",new JvmParamDesc( "打印花费在JIT编译上的时间"));
		
		paramMap.put("-XX:ErrorFile",new JvmParamDesc( "错误文件"));
		paramMap.put("-XX:-ExtendedDTraceProbes",new JvmParamDesc( "启用DTrace探测器"));
		paramMap.put("-XX:HeapDumpPath",new JvmParamDesc( "指定HeapDump的文件路径或目录"));
		paramMap.put("-XX:-HeapDumpOnOutOfMemoryError",new JvmParamDesc( "当抛出OOM时进行HeapDump"));
		paramMap.put("-XX:OnError",new JvmParamDesc( "当发生错误时执行用户指定的命令"));
		
		paramMap.put("-XX:OnOutOfMemoryError",new JvmParamDesc( "当发生OOM时执行用户指定的命令"));
		paramMap.put("-XX:-PrintClassHistogram",new JvmParamDesc( "当Ctrl+Break发生时打印Class实例信息,与jmap -histo相同"));
		paramMap.put("-XX:-PrintConcurrentLocks",new JvmParamDesc( "当Ctrl+Break发生时打印java.util.concurrent的锁信息, 与jstack -l相同"));
		paramMap.put("-XX:-PrintCommandLineFlags",new JvmParamDesc( "打印命令行上的标记"));
		paramMap.put("-XX:-PrintCompilation",new JvmParamDesc( "当方法被编译时打印信息"));
		
		paramMap.put("-XX:-PrintGC",new JvmParamDesc( "当GC发生时打印信息"));
		paramMap.put("-XX:+PrintGCDateStamps",new JvmParamDesc( "输出GC的时间戳（以日期的形式，如 2013-05-04T21:53:59.234+0800）"));
		paramMap.put("-XX:+PrintGCDetails",new JvmParamDesc( "输出GC的详细日志"));
		
		paramMap.put("-XX:-PrintGCTimeStamps",new JvmParamDesc( "打印GC用时"));
		paramMap.put("-XX:-PrintTenuringDistribution",new JvmParamDesc( "打印Tenuring年龄信息"));
		paramMap.put("-XX:-TraceClassLoading",new JvmParamDesc( "跟踪类加载"));
		paramMap.put("-XX:-TraceClassLoadingPreorder",new JvmParamDesc( "跟踪所有加载的引用类"));
		
		
		paramMap.put("-XX:-TraceClassResolution",new JvmParamDesc( "跟踪常量池的变化"));
		paramMap.put("-XX:-TraceClassUnloading",new JvmParamDesc( "	跟踪类的卸载"));
		paramMap.put("-XX:-TraceLoaderConstraints",new JvmParamDesc( "跟踪常量的加载"));
		paramMap.put("-XX:+PerfSaveDataToFile",new JvmParamDesc( "退出时保存jvmstat二进制文件"));
		paramMap.put("-XX:+UseCompressedOops",new JvmParamDesc( "使用压缩的对象指针"));
		
		
		paramMap.put("-XX:+AlwaysPreTouch",new JvmParamDesc( "	Pre-touch the Java heap during JVM initialization. Every page of the heap is thus demand-zeroed during initialization rather than incrementally during application execution."));
		paramMap.put("-XX:AllocatePrefetchDistance",new JvmParamDesc( "Sets the prefetch distance for object allocation. Memory about to be written with the value of new objects is prefetched into cache at this distance (in bytes) beyond the address of the last allocated object. Each Java thread has its own allocation point. The default value varies with the platform on which the JVM is running."));
		paramMap.put("-XX:InlineSmallCode",new JvmParamDesc( "当编译的代码小于指定的值时,内联编译的代码"));
		paramMap.put("-XX:MaxInlineSize",new JvmParamDesc( "内联方法的最大字节数"));
		paramMap.put("-XX:FreqInlineSize",new JvmParamDesc( "内联频繁执行的方法的最大字节码大小"));
		
		paramMap.put("-XX:LoopUnrollLimi",new JvmParamDesc( "	Unroll loop bodies with server compiler intermediate representation node count less than this value. The limit used by the server compiler is a function of this value, not the actual value. The default value varies with the platform on which the JVM is running."));
		paramMap.put("-XX:InitialTenuringThreshold",new JvmParamDesc( "设置初始的对象在新生代中最大存活次数"));
		paramMap.put("-XX:MaxTenuringThreshold",new JvmParamDesc( "设置对象在新生代中最大的存活次数,最大值15,并行回收机制默认为15,CMS默认为4"));
		 
		
		//G1
		paramMap.put("-XX:G1HeapRegionSize",new JvmParamDesc( "设置的 G1 区域的大小。值是 2 的幂，范围是 1 MB 到 32 MB 之间。目标是根据最小的 Java 堆大小划分出约 2048 个区域"));
		paramMap.put("-XX:MaxGCPauseMillis",new JvmParamDesc( "为所需的最长暂停时间设置目标值。默认值是 200 毫秒。指定的值不适用于您的堆大小"));
		paramMap.put("-XX:G1NewSizePercent",new JvmParamDesc( "设置要用作年轻代大小最小值的堆百分比。默认值是 Java 堆的 5%。这是一个实验性的标志。有关示例，请参见“如何解锁实验性虚拟机标志”。此设置取代了 -XX:DefaultMinNewGenPercent 设置"));
		paramMap.put("-XX:G1MaxNewSizePercent",new JvmParamDesc( "要用作年轻代大小最大值的堆大小百分比。默认值是 Java 堆的 60%。这是一个实验性的标志。有关示例，请参见“如何解锁实验性虚拟机标志”。此设置取代了 -XX:DefaultMaxNewGenPercent 设置"));
		paramMap.put("-XX:ParallelGCThreads",new JvmParamDesc( "STW 工作线程数的值。将 n 的值设置为逻辑处理器的数量。n 的值与逻辑处理器的数量相同，最多为 8。如果逻辑处理器不止八个，则将 n 的值设置为逻辑处理器数的 5/8 左右。这适用于大多数情况，除非是较大的 SPARC 系统，其中 n 的值可以是逻辑处理器数的 5/16 左右。"));
		
		paramMap.put("-XX:ConcGCThreads",new JvmParamDesc( "并行标记的线程数。将 n 设置为并行垃圾回收线程数 (ParallelGCThreads) 的 1/4 左右"));
		paramMap.put("-XX:InitiatingHeapOccupancyPercent",new JvmParamDesc( "触发标记周期的 Java 堆占用率阈值。默认占用率是整个 Java 堆的 45%。"));
		paramMap.put("-XX:G1MixedGCLiveThresholdPercent",new JvmParamDesc( "为混合垃圾回收周期中要包括的旧区域设置占用率阈值。默认占用率为 65%。这是一个实验性的标志。有关示例，请参见“如何解锁实验性虚拟机标志”。此设置取代了 -XX:G1OldCSetRegionLiveThresholdPercent 设置。Java HotSpot VM build 23 中没有此设置。"));
		paramMap.put("-XX:G1HeapWastePercent",new JvmParamDesc( "设置您愿意浪费的堆百分比。如果可回收百分比小于堆废物百分比，Java HotSpot VM 不会启动混合垃圾回收周期。默认值是 10%。Java HotSpot VM build 23 中没有此设置"));
		paramMap.put("-XX:G1MixedGCCountTarget",new JvmParamDesc( "设置标记周期完成后，对存活数据上限为 G1MixedGCLIveThresholdPercent 的旧区域执行混合垃圾回收的目标次数。默认值是 8 次混合垃圾回收。混合回收的目标是要控制在此目标次数以内。Java HotSpot VM build 23 中没有此设置。"));
		
		
		paramMap.put("-XX:G1OldCSetRegionThresholdPercent",new JvmParamDesc( "混合垃圾回收期间要回收的最大旧区域数。默认值是 Java 堆的 10%。Java HotSpot VM build 23 中没有此设置"));
		paramMap.put("-XX:G1ReservePercent",new JvmParamDesc( "设置作为空闲空间的预留内存百分比，以降低目标空间溢出的风险。默认值是 10%。增加或减少百分比时，请确保对总的 Java 堆调整相同的量。Java HotSpot VM build 23 中没有此设置。"));
		paramMap.put("-XX:+UnlockExperimentalVMOptions",new JvmParamDesc( "解锁实验性虚拟机标志"));
		paramMap.put("-XX:G1ReservePercent",new JvmParamDesc( "“目标空间”预留内存量"));
		
		//
		paramMap.put("-XX:MetaspaceSize", new JvmParamDesc("初始空间大小，达到该值就会触发垃圾收集进行类型卸载，同时GC会对该值进行调整：如果释放了大量的空间，就适当降低该值；如果释放了很少的空间，那么在不超过MaxMetaspaceSize时，适当提高该值"));
		paramMap.put("-XX:MaxMetaspaceSize", new JvmParamDesc("最大空间，默认是没有限制的"));
		paramMap.put("-XX:G1RSetUpdatingPauseTimePercent", new JvmParamDesc("设置GC evacuation（疏散）阶段期间G1 GC更新RSets消耗时间的百分比（默认是目标停顿时间的10%）。你可以增大或减小百分比的值，以便在stop-the-world(STW)GC阶段花费更多或更少的时间，让concurrent refinement thread处理相应的缓冲区。减少百分比的值，你在推迟concurrent refinement thread的工作；因此，你会看到并发任务增加"));
		paramMap.put("-XX:+UseStringDeduplication", new JvmParamDesc("使用字符串去重机制"));
		paramMap.put("-XX:StringDeduplicationAgeThreshold", new JvmParamDesc("字符串存活的最小年龄 ，默认是3"));
		paramMap.put("-XX:+ParallelRefProcEnabled", new JvmParamDesc("用多个的引用处理线程，而不是单个线程。这个选项不会启用多线程运行方法的finalizer。他会使用很多线程去发现需要排队通知的finalizable对象"));
		paramMap.put("-XX:+PrintAdaptiveSizePolicy", new JvmParamDesc("打印自适应收集的大小。默认关闭"));
		
		paramMap.put("-XX:+PrintTenuringDistribution", new JvmParamDesc("打印对各代信息。"));
		/**
		 * 强制要求JVM始终抛出含堆栈的异常(-XX:-OmitStackTraceInFastThrow) 问题描述：生产环境抛异常,但却没有将堆栈信息输出到日志,可以确定的是日志输出时用的是log.error("xx发生错误", e) 问题分析：它跟JDK5的一个新特性有关,对于一些频繁抛出的异常,JDK为了性能会做一个优化,即JIT重新编译后会抛出没有堆栈的异常           而在使用-server模式时,该优化选项是开启的,因此在频繁抛出某个异常一段时间后,该优化开始起作用,即只抛出没有堆栈的异常信息 问题解决：由于该优化是在JIT重新编译后才起作用,因此起初抛出的异常还是有堆栈的,所以可以查看较旧的日志,寻找完整的堆栈信息           另一个解决办法是暂时禁用该优化,即强制要求每次都要抛出有堆栈的异常,幸好JDK提供了通过配置JVM参数的方式来关闭该优化           即-XX:-OmitStackTraceInFastThrow,便可禁用该优化了(注意选项中的减号,加号则表示启用) 官方说明：The compiler in the server VM now provides correct stack backtraces for all "cold" built-in exceptions.          For performance purposes, when such an exception is thrown a few times, the method may be recompiled.          After recompilation, the compiler may choose a faster tactic using preallocated exceptions that do not provide a stack trace.          To disable completely the use of preallocated exceptions, use this new flag: -XX:-OmitStackTraceInFastThrow.

		 * */
		paramMap.put("-XX:-OmitStackTraceInFastThrow", new JvmParamDesc("强制要求JVM始终抛出含堆栈的异常"));
		
		paramMap.put("-XX:MinMetaspaceFreeRatio", new JvmParamDesc("扩大空间的最小比率，当GC后，内存占用超过这一比率，就会扩大空间"));
		paramMap.put("-XX:MaxMetaspaceFreeRatio", new JvmParamDesc(" 缩小空间的最小比率，当GC后，内存占用低于这一比率，就会缩小空间"));
		
		
		
	}
	public static class JvmParamDesc{
		
		private int startVM;
		private boolean defaultTurnon=false;
		private String desc;
		
		public JvmParamDesc(int start,boolean defaultTurnon,String desc) {
			this.startVM=start;
			this.defaultTurnon=defaultTurnon;
			this.desc=desc;
		}
		public JvmParamDesc(boolean defaultTurnon,String desc) {
			this.defaultTurnon=defaultTurnon;
			this.desc=desc;
		}
		public JvmParamDesc( String desc) {
		 
			this.desc=desc;
		}
		public int getStartVM() {
			return startVM;
		}
		public void setStartVM(int startVM) {
			this.startVM = startVM;
		}
		public boolean isDefaultTurnon() {
			return defaultTurnon;
		}
		public void setDefaultTurnon(boolean defaultTurnon) {
			this.defaultTurnon = defaultTurnon;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		String[] params=readToLine("startup.param.txt").split(" ");
		for(int i=0;i<params.length;i++){
			String param=params[i];
			parseParam(param,i,params);
		}
		
	}
	
	public static void parseParam(String param,int idx,String[] params){
		
		if(StringUtils.isEmpty(param) || StringUtils.isNumeric(param)){
			//ignore 
			return ;
		}
		//FIXME 第一期先打印出来中文释义
		 
		for(String p:paramMap.keySet()){
			if(StringUtils.isEmpty(p)){continue;}
			if (param.startsWith(p)){//匹配 
				 
				formatOut(param, paramMap.get(p).getDesc() );
				return;
			} 
		} 
		
		formatOut(param, "unknown");
	} 
	
	public static void formatOut(String key,String desc){
		System.out.println(key+"|	"+desc );
	}
	public static String readToLine(String fileName) throws IOException{
		InputStream is=JvmStartUpParamParser.class.getResourceAsStream(fileName);
		return FileUtils.readWholeFileAsUTF8(is);
		
	}
}
