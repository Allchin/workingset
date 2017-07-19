for dbIdx=0,7 do
	redis.call("select",dbIdx);
	local ks=redis.call("keys","u:uid:20150527204539RyXwFk*");
	for k in ks 
	do
		local v=redis.call("get",k);
		//8869797#0#u:e:panyongjun26@163.com
		local line=v+"#"+dbIdx+"#"+k
		print line
	end
	
end

