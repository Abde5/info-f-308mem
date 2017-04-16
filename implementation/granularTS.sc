(
SynthDef("GranularTS", {arg out=0, bufnum=0, in=1, thresh = 0, duration = 0.1, rate = 0.7;
	
	var bufferFrames, bufRateScale, trigger, env, envGen, bufPointer,sig;
	
	bufferFrames = BufFrames.kr(bufnum);
	bufRateScale = BufRateScale.kr(bufnum);
	trigger = Trig1.ar(SinOsc.ar( 1/(duration*0.7) ),duration*0.06);
	env = Env([0,1,0],[duration/2,duration/2],'sine');
	envGen = EnvGen.kr(env, trigger);
	
	bufPointer = Line.ar(0,bufferFrames,BufDur.kr(bufnum)*rate);
	
	sig = PlayBuf.ar(1, bufnum, bufRateScale, trigger, bufPointer)*envGen;
	
	Out.ar(out,Pan2.ar(sig));
  }).play(s,[\out, 0, \bufnum, b.bufnum]);
)