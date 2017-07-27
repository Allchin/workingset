package cn.allchin.jvm.param;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hp.hpl.jena.util.FileUtils;

/**
 *  jvm����������������
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
 
		paramMap.put("-Xms",new JvmParamDesc( "��ʼ���ڴ�"));
		paramMap.put("-Xmx",new JvmParamDesc( "�����ڴ�"));
		
		
		paramMap.put("-server",new JvmParamDesc( "������ģʽ"));
		paramMap.put("-XX:+DisableExplicitGC",new JvmParamDesc( "��ֹ����������ʽ�ص���System.gc()"));
		paramMap.put("-verbose:gc",new JvmParamDesc( "����������GC����ϸ���[Full GC 168K->97K(1984K)�� 0.0253873 secs]"));

		paramMap.put("-Xloggc",new JvmParamDesc( "gc��־������·��"));
		
		paramMap.put("-Djava.endorsed.dirs",new JvmParamDesc( "ָ����Ŀ¼����õ�jar�ļ������и���ϵͳAPI�Ĺ���"));
		paramMap.put("-classpath",new JvmParamDesc( "��·��"));
		
		paramMap.put("-Xss",new JvmParamDesc( "�߳�ջ��С"));
		paramMap.put("-XX:PermSize",new JvmParamDesc( "���ô���ʼֵ"));
		
		paramMap.put("-XX:MaxDirectMemorySize",new JvmParamDesc( "ֱ���ڴ����ֵ"));
		paramMap.put("-cp",new JvmParamDesc( ""));
		paramMap.put("-n",new JvmParamDesc( ""));
		paramMap.put("-XX:NewRatio",new JvmParamDesc( "�����������ϴ��Ķ��ڴ�ռ�ñ���, ����2��ʾ������ռ���ϴ���1/2"));
		paramMap.put("-XX:NewSize",new JvmParamDesc( "������ռ�������ڴ�����ֵ"));
		 
		paramMap.put("-XX:-AllowUserSignalHandlers",new JvmParamDesc( "����Ϊjava���̰�װ�źŴ�����,�źŴ���μ���:sun.misc.Signal, sun.misc.SignalHandler"));
		paramMap.put("-XX:+FailOverToOldVerifier",new JvmParamDesc( "����µ�ClassУ�������ʧ�ܣ���ʹ���ϵ�У����(ʧ��ԭ��:��ΪJDK6������¼��ݵ�JDK1.2����JDK1.2��class info ��JDK6��info���ڽϴ�Ĳ��죬������У�������ܻ����У��ʧ�ܵ����)"));
		paramMap.put("-XX:+HandlePromotionFailure",new JvmParamDesc( "�ر��������ռ�����"));
		paramMap.put("-XX:+MaxFDLimit",new JvmParamDesc( "����java���̿����ļ�������Ϊ����ϵͳ��������ֵ��"));
		paramMap.put("-XX:PreBlockSpin",new JvmParamDesc( "���ƶ��߳��������Ż�����������"));
		paramMap.put("-XX:-RelaxAccessControlCheck",new JvmParamDesc( "��ClassУ�����У����ɶԷ��ʿ��Ƶļ��,������reflection���setAccessible����"));
		paramMap.put("-XX:+ScavengeBeforeFullGC",new JvmParamDesc( "	��Full GCǰ����һ��Minor GC"));
		paramMap.put("-XX:+UseAltSigs",new JvmParamDesc( "Ϊ�˷�ֹ�����������źŵ�Ӧ�ó����ͻ������ʹ�ú��ź���� SIGUSR1��SIGUSR2"));
		paramMap.put("-XX:+UseBoundThreads",new JvmParamDesc( "�����е��û��̵߳��ں��߳�, �����߳̽��뼢��״̬���ò����κ�cpu time���Ĵ���"));
		
		paramMap.put("-XX:-UseConcMarkSweepGC",new JvmParamDesc( "����CMS��ͣ�������ռ���,����FGC����ͣʱ��"));
		paramMap.put("-XX:+UseGCOverheadLimit",new JvmParamDesc(true, "����GC������ʱ�䡣���GC��ʱ����������OOM"));
		
		paramMap.put("-XX:+UseLWPSynchronization",new JvmParamDesc( "ʹ�����������̣��ں��̣߳��滻�߳�ͬ��"));
		paramMap.put("-XX:-UseParallelGC",new JvmParamDesc( "����Ϊ������ʹ�ò�����������ϴ�ʹ�õ��߳�Mark-Sweep-Compact�������ռ���"));
		paramMap.put("-XX:-UseParallelOldGC",new JvmParamDesc( "����Ϊ���������������ʹ�ò�������������ռ���"));
		paramMap.put("-XX:-UseSerialGC",new JvmParamDesc("ʹ�ô��������ռ���"));
		paramMap.put("-XX:-UseSpinning",new JvmParamDesc(16,true, "���ö��߳��������Ż�"));
		paramMap.put("-XX:+UseTLAB",new JvmParamDesc( "�����̱߳��ػ�����"));
		paramMap.put("-XX:+UseSplitVerifier",new JvmParamDesc( "ʹ���µ�Class����У����"));
		paramMap.put("-XX:+UseThreadPriorities",new JvmParamDesc( 16,true,"ʹ�ñ����̵߳����ȼ�"));
		
		paramMap.put("-XX:+UseVMInterruptibleIO",new JvmParamDesc( "��solaris�У���������ʱ�ж��߳�"));
		//����
		paramMap.put("-XX:+AggressiveOpts",new JvmParamDesc( "����JVM�����Ŷ����µĵ��ųɹ�����������Ż���ƫ�������������ϴ��ռ���"));
		paramMap.put("-XX:CompileThreshold",new JvmParamDesc( "ͨ��JIT������������������ɻ�����Ĵ�����ֵ���������Ϊ���÷����Ĵ����������1000�Σ�����������Ϊ������"));
		paramMap.put("-XX:LargePageSizeInBytes",new JvmParamDesc( "���ö��ڴ���ڴ�ҳ��С"));
		
		paramMap.put("-XX:MaxHeapFreeRatio",new JvmParamDesc( "GC��������ֿ��ж��ڴ�ռ������Ԥ������ֵ��70%��������Ԥ������ֵ"));
		paramMap.put("-XX:MaxNewSize",new JvmParamDesc( "������ռ�������ڴ�����ֵ"));
		paramMap.put("-XX:MaxPermSize",new JvmParamDesc( "Perm���׳Ʒ�������ռ�������ڴ�����ֵ"));
		paramMap.put("-XX:MinHeapFreeRatio",new JvmParamDesc( "GC��������ֿ��ж��ڴ�ռ������Ԥ������ֵ��40%������������ֵ"));
		
		
		paramMap.put("-XX:ReservedCodeCacheSize",new JvmParamDesc( "���ô��뻺������ֵ������ʱ��"));
		paramMap.put("-XX:SurvivorRatio",new JvmParamDesc( "Eden��Survivor��ռ�ñ���������8��ʾ��һ��survivor��ռ�� 1/8 ��Eden�ڴ棬��1/10���������ڴ棬Ϊʲô����1/9����Ϊ���ǵ���������2��survivor����S0��S1������survivor�ܹ���ռ���������ڴ�� 2/10��Eden����������ռ����Ϊ 8/10"));
		paramMap.put("-XX:TargetSurvivorRatio",new JvmParamDesc( "ʵ��ʹ�õ�survivor�ռ��Сռ�ȡ�Ĭ����50%�����90%"));
		paramMap.put("-XX:ThreadStackSize",new JvmParamDesc( "�̶߳�ջ��С"));
		paramMap.put("-XX:+UseBiasedLocking",new JvmParamDesc( "����ƫ����"));
		
		paramMap.put("-XX:+UseFastAccessorMethods",new JvmParamDesc( "�Ż�ԭʼ���͵�getter��������(get/set:Primitive Type)"));
		paramMap.put("-XX:-UseISM",new JvmParamDesc( "����solaris��ISM"));
		paramMap.put("-XX:+UseLargePages",new JvmParamDesc( "���ô��ڴ��ҳ"));
		paramMap.put("-XX:+UseMPSS",new JvmParamDesc( "����solaris��MPSS��������ISMͬʱʹ��"));
		paramMap.put("-XX:+UseStringCache",new JvmParamDesc(true, "���û��泣�õ��ַ�����"));
		
		paramMap.put("-XX:AllocatePrefetchLines",new JvmParamDesc( "Number of cache lines to load after the last object allocation using prefetch instructions generated in JIT compiled code. Default values are 1 if the last allocated object was an instance and 3 if it was an array."));
		paramMap.put("-XX:AllocatePrefetchStyle",new JvmParamDesc( "Generated code style for prefetch instructions.0 �C no prefetch instructions are generate*d*,1 �C execute prefetch instructions after each allocation,2 �C use TLAB allocation watermark pointer to gate when prefetch instructions are executed."));
		paramMap.put("-XX:+UseCompressedStrings",new JvmParamDesc( "���У����ڲ���Ҫ16λ�ַ����ַ���������ʹ��byte[] ����char[]���������Ӧ�ã�����Խ�ʡ�ڴ棬���ٶȽ�����5��-10����"));
		paramMap.put("-XX:+OptimizeStringConcat",new JvmParamDesc( "�Ż��ַ������Ӳ����ڿ��ܵ������"));
		
		//����
		paramMap.put("-XX:-CITime",new JvmParamDesc( "��ӡ������JIT�����ϵ�ʱ��"));
		
		paramMap.put("-XX:ErrorFile",new JvmParamDesc( "�����ļ�"));
		paramMap.put("-XX:-ExtendedDTraceProbes",new JvmParamDesc( "����DTrace̽����"));
		paramMap.put("-XX:HeapDumpPath",new JvmParamDesc( "ָ��HeapDump���ļ�·����Ŀ¼"));
		paramMap.put("-XX:-HeapDumpOnOutOfMemoryError",new JvmParamDesc( "���׳�OOMʱ����HeapDump"));
		paramMap.put("-XX:OnError",new JvmParamDesc( "����������ʱִ���û�ָ��������"));
		
		paramMap.put("-XX:OnOutOfMemoryError",new JvmParamDesc( "������OOMʱִ���û�ָ��������"));
		paramMap.put("-XX:-PrintClassHistogram",new JvmParamDesc( "��Ctrl+Break����ʱ��ӡClassʵ����Ϣ,��jmap -histo��ͬ"));
		paramMap.put("-XX:-PrintConcurrentLocks",new JvmParamDesc( "��Ctrl+Break����ʱ��ӡjava.util.concurrent������Ϣ, ��jstack -l��ͬ"));
		paramMap.put("-XX:-PrintCommandLineFlags",new JvmParamDesc( "��ӡ�������ϵı��"));
		paramMap.put("-XX:-PrintCompilation",new JvmParamDesc( "������������ʱ��ӡ��Ϣ"));
		
		paramMap.put("-XX:-PrintGC",new JvmParamDesc( "��GC����ʱ��ӡ��Ϣ"));
		paramMap.put("-XX:+PrintGCDateStamps",new JvmParamDesc( "���GC��ʱ����������ڵ���ʽ���� 2013-05-04T21:53:59.234+0800��"));
		paramMap.put("-XX:+PrintGCDetails",new JvmParamDesc( "���GC����ϸ��־"));
		
		paramMap.put("-XX:-PrintGCTimeStamps",new JvmParamDesc( "��ӡGC��ʱ"));
		paramMap.put("-XX:-PrintTenuringDistribution",new JvmParamDesc( "��ӡTenuring������Ϣ"));
		paramMap.put("-XX:-TraceClassLoading",new JvmParamDesc( "���������"));
		paramMap.put("-XX:-TraceClassLoadingPreorder",new JvmParamDesc( "�������м��ص�������"));
		
		
		paramMap.put("-XX:-TraceClassResolution",new JvmParamDesc( "���ٳ����صı仯"));
		paramMap.put("-XX:-TraceClassUnloading",new JvmParamDesc( "	�������ж��"));
		paramMap.put("-XX:-TraceLoaderConstraints",new JvmParamDesc( "���ٳ����ļ���"));
		paramMap.put("-XX:+PerfSaveDataToFile",new JvmParamDesc( "�˳�ʱ����jvmstat�������ļ�"));
		paramMap.put("-XX:+UseCompressedOops",new JvmParamDesc( "ʹ��ѹ���Ķ���ָ��"));
		
		
		paramMap.put("-XX:+AlwaysPreTouch",new JvmParamDesc( "	Pre-touch the Java heap during JVM initialization. Every page of the heap is thus demand-zeroed during initialization rather than incrementally during application execution."));
		paramMap.put("-XX:AllocatePrefetchDistance",new JvmParamDesc( "Sets the prefetch distance for object allocation. Memory about to be written with the value of new objects is prefetched into cache at this distance (in bytes) beyond the address of the last allocated object. Each Java thread has its own allocation point. The default value varies with the platform on which the JVM is running."));
		paramMap.put("-XX:InlineSmallCode",new JvmParamDesc( "������Ĵ���С��ָ����ֵʱ,��������Ĵ���"));
		paramMap.put("-XX:MaxInlineSize",new JvmParamDesc( "��������������ֽ���"));
		paramMap.put("-XX:FreqInlineSize",new JvmParamDesc( "����Ƶ��ִ�еķ���������ֽ����С"));
		
		paramMap.put("-XX:LoopUnrollLimi",new JvmParamDesc( "	Unroll loop bodies with server compiler intermediate representation node count less than this value. The limit used by the server compiler is a function of this value, not the actual value. The default value varies with the platform on which the JVM is running."));
		paramMap.put("-XX:InitialTenuringThreshold",new JvmParamDesc( "���ó�ʼ�Ķ���������������������"));
		paramMap.put("-XX:MaxTenuringThreshold",new JvmParamDesc( "���ö����������������Ĵ�����,���ֵ15,���л��ջ���Ĭ��Ϊ15,CMSĬ��Ϊ4"));
		 
		
		//G1
		paramMap.put("-XX:G1HeapRegionSize",new JvmParamDesc( "���õ� G1 ����Ĵ�С��ֵ�� 2 ���ݣ���Χ�� 1 MB �� 32 MB ֮�䡣Ŀ���Ǹ�����С�� Java �Ѵ�С���ֳ�Լ 2048 ������"));
		paramMap.put("-XX:MaxGCPauseMillis",new JvmParamDesc( "Ϊ��������ͣʱ������Ŀ��ֵ��Ĭ��ֵ�� 200 ���롣ָ����ֵ�����������ĶѴ�С"));
		paramMap.put("-XX:G1NewSizePercent",new JvmParamDesc( "����Ҫ�����������С��Сֵ�ĶѰٷֱȡ�Ĭ��ֵ�� Java �ѵ� 5%������һ��ʵ���Եı�־���й�ʾ������μ�����ν���ʵ�����������־����������ȡ���� -XX:DefaultMinNewGenPercent ����"));
		paramMap.put("-XX:G1MaxNewSizePercent",new JvmParamDesc( "Ҫ�����������С���ֵ�ĶѴ�С�ٷֱȡ�Ĭ��ֵ�� Java �ѵ� 60%������һ��ʵ���Եı�־���й�ʾ������μ�����ν���ʵ�����������־����������ȡ���� -XX:DefaultMaxNewGenPercent ����"));
		paramMap.put("-XX:ParallelGCThreads",new JvmParamDesc( "STW �����߳�����ֵ���� n ��ֵ����Ϊ�߼���������������n ��ֵ���߼���������������ͬ�����Ϊ 8������߼���������ֹ�˸����� n ��ֵ����Ϊ�߼����������� 5/8 ���ҡ��������ڴ��������������ǽϴ�� SPARC ϵͳ������ n ��ֵ�������߼����������� 5/16 ���ҡ�"));
		
		paramMap.put("-XX:ConcGCThreads",new JvmParamDesc( "���б�ǵ��߳������� n ����Ϊ�������������߳��� (ParallelGCThreads) �� 1/4 ����"));
		paramMap.put("-XX:InitiatingHeapOccupancyPercent",new JvmParamDesc( "����������ڵ� Java ��ռ������ֵ��Ĭ��ռ���������� Java �ѵ� 45%��"));
		paramMap.put("-XX:G1MixedGCLiveThresholdPercent",new JvmParamDesc( "Ϊ�����������������Ҫ�����ľ���������ռ������ֵ��Ĭ��ռ����Ϊ 65%������һ��ʵ���Եı�־���й�ʾ������μ�����ν���ʵ�����������־����������ȡ���� -XX:G1OldCSetRegionLiveThresholdPercent ���á�Java HotSpot VM build 23 ��û�д����á�"));
		paramMap.put("-XX:G1HeapWastePercent",new JvmParamDesc( "������Ը���˷ѵĶѰٷֱȡ�����ɻ��հٷֱ�С�ڶѷ���ٷֱȣ�Java HotSpot VM ����������������������ڡ�Ĭ��ֵ�� 10%��Java HotSpot VM build 23 ��û�д�����"));
		paramMap.put("-XX:G1MixedGCCountTarget",new JvmParamDesc( "���ñ��������ɺ󣬶Դ����������Ϊ G1MixedGCLIveThresholdPercent �ľ�����ִ�л���������յ�Ŀ�������Ĭ��ֵ�� 8 �λ���������ա���ϻ��յ�Ŀ����Ҫ�����ڴ�Ŀ��������ڡ�Java HotSpot VM build 23 ��û�д����á�"));
		
		
		paramMap.put("-XX:G1OldCSetRegionThresholdPercent",new JvmParamDesc( "������������ڼ�Ҫ���յ�������������Ĭ��ֵ�� Java �ѵ� 10%��Java HotSpot VM build 23 ��û�д�����"));
		paramMap.put("-XX:G1ReservePercent",new JvmParamDesc( "������Ϊ���пռ��Ԥ���ڴ�ٷֱȣ��Խ���Ŀ��ռ�����ķ��ա�Ĭ��ֵ�� 10%�����ӻ���ٰٷֱ�ʱ����ȷ�����ܵ� Java �ѵ�����ͬ������Java HotSpot VM build 23 ��û�д����á�"));
		paramMap.put("-XX:+UnlockExperimentalVMOptions",new JvmParamDesc( "����ʵ�����������־"));
		paramMap.put("-XX:G1ReservePercent",new JvmParamDesc( "��Ŀ��ռ䡱Ԥ���ڴ���"));
		
		//
		paramMap.put("-XX:MetaspaceSize", new JvmParamDesc("��ʼ�ռ��С���ﵽ��ֵ�ͻᴥ�������ռ���������ж�أ�ͬʱGC��Ը�ֵ���е���������ͷ��˴����Ŀռ䣬���ʵ����͸�ֵ������ͷ��˺��ٵĿռ䣬��ô�ڲ�����MaxMetaspaceSizeʱ���ʵ���߸�ֵ"));
		paramMap.put("-XX:MaxMetaspaceSize", new JvmParamDesc("���ռ䣬Ĭ����û�����Ƶ�"));
		paramMap.put("-XX:G1RSetUpdatingPauseTimePercent", new JvmParamDesc("����GC evacuation����ɢ���׶��ڼ�G1 GC����RSets����ʱ��İٷֱȣ�Ĭ����Ŀ��ͣ��ʱ���10%���������������С�ٷֱȵ�ֵ���Ա���stop-the-world(STW)GC�׶λ��Ѹ������ٵ�ʱ�䣬��concurrent refinement thread������Ӧ�Ļ����������ٰٷֱȵ�ֵ�������Ƴ�concurrent refinement thread�Ĺ�������ˣ���ῴ��������������"));
		paramMap.put("-XX:+UseStringDeduplication", new JvmParamDesc("ʹ���ַ���ȥ�ػ���"));
		paramMap.put("-XX:StringDeduplicationAgeThreshold", new JvmParamDesc("�ַ���������С���� ��Ĭ����3"));
		paramMap.put("-XX:+ParallelRefProcEnabled", new JvmParamDesc("�ö�������ô����̣߳������ǵ����̡߳����ѡ������ö��߳����з�����finalizer������ʹ�úܶ��߳�ȥ������Ҫ�Ŷ�֪ͨ��finalizable����"));
		paramMap.put("-XX:+PrintAdaptiveSizePolicy", new JvmParamDesc("��ӡ����Ӧ�ռ��Ĵ�С��Ĭ�Ϲر�"));
		
		paramMap.put("-XX:+PrintTenuringDistribution", new JvmParamDesc("��ӡ�Ը�����Ϣ��"));
		/**
		 * ǿ��Ҫ��JVMʼ���׳�����ջ���쳣(-XX:-OmitStackTraceInFastThrow) ���������������������쳣,��ȴû�н���ջ��Ϣ�������־,����ȷ��������־���ʱ�õ���log.error("xx��������", e) �������������JDK5��һ���������й�,����һЩƵ���׳����쳣,JDKΪ�����ܻ���һ���Ż�,��JIT���±������׳�û�ж�ջ���쳣           ����ʹ��-serverģʽʱ,���Ż�ѡ���ǿ�����,�����Ƶ���׳�ĳ���쳣һ��ʱ���,���Ż���ʼ������,��ֻ�׳�û�ж�ջ���쳣��Ϣ �����������ڸ��Ż�����JIT���±�����������,�������׳����쳣�����ж�ջ��,���Կ��Բ鿴�Ͼɵ���־,Ѱ�������Ķ�ջ��Ϣ           ��һ������취����ʱ���ø��Ż�,��ǿ��Ҫ��ÿ�ζ�Ҫ�׳��ж�ջ���쳣,�Һ�JDK�ṩ��ͨ������JVM�����ķ�ʽ���رո��Ż�           ��-XX:-OmitStackTraceInFastThrow,��ɽ��ø��Ż���(ע��ѡ���еļ���,�Ӻ����ʾ����) �ٷ�˵����The compiler in the server VM now provides correct stack backtraces for all "cold" built-in exceptions.          For performance purposes, when such an exception is thrown a few times, the method may be recompiled.          After recompilation, the compiler may choose a faster tactic using preallocated exceptions that do not provide a stack trace.          To disable completely the use of preallocated exceptions, use this new flag: -XX:-OmitStackTraceInFastThrow.

		 * */
		paramMap.put("-XX:-OmitStackTraceInFastThrow", new JvmParamDesc("ǿ��Ҫ��JVMʼ���׳�����ջ���쳣"));
		
		paramMap.put("-XX:MinMetaspaceFreeRatio", new JvmParamDesc("����ռ����С���ʣ���GC���ڴ�ռ�ó�����һ���ʣ��ͻ�����ռ�"));
		paramMap.put("-XX:MaxMetaspaceFreeRatio", new JvmParamDesc(" ��С�ռ����С���ʣ���GC���ڴ�ռ�õ�����һ���ʣ��ͻ���С�ռ�"));
		
		paramMap.put("-XX:+HeapDumpOnOutOfMemoryError", new JvmParamDesc("OOMʱdump���ļ�"));
		paramMap.put("-XX:HeapDumpPath", new JvmParamDesc("dump���ļ���·��"));
		
		
	 
		
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
		//FIXME ��һ���ȴ�ӡ������������
		 
		for(String p:paramMap.keySet()){
			if(StringUtils.isEmpty(p)){continue;}
			if (param.startsWith(p)){//ƥ�� 
				 
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
