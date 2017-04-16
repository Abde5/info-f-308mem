(use 'overtone.core)
(connect-external-server 57110)

;; synth that plays a buffer, I took it from the overtone source code

(defsynth stereo-partial-player
  "Plays a stereo buffer from start pos to end pos (represented as
   values between 0 and 1). May be looped via the loop?
   argument. Release time is the release phase after the looping has
   finished to remove clipping."
  [buf 0 rate 1 start 0 end 1 loop? 0 amp 1 release 0 out-bus 0]
  (let [n-frames  (buf-frames buf)
        rate      (* rate (buf-rate-scale buf))
        start-pos (* start n-frames)
        end-pos   (* end n-frames)
        phase     (phasor:ar :start start-pos :end end-pos :rate rate)
        snd       (buf-rd 2 buf phase)
        e-gate    (+ loop?
                     (a2k (latch:ar (line 1 0 0.0001) (bpz2 phase))))
        env       (env-gen (asr 0 1 release) :gate e-gate :action FREE)]
    (out out-bus (* amp env snd))))


;; Granular live time stretching algorithm (AKA SOLA)
(defsynth granulator
  "commentaire à écrire"
  [buf 0 rate 1 gr-dur 0.1 start 0 end 1 out-bus 0 loop? 0 amp 1]
  (let [n-frames     (buf-frames buf)
        buf-rate     (buf-rate-scale buf)
        rate         (* rate buf-rate)
        start-pos    (* start n-frames)
        end-pos      (* end n-frames)
        trigger      (trig1:kr (sin-osc:kr ( / 1 (* gr-dur 0.7))) (* gr-dur 0.4))
        gr-env       (env-gen:kr (envelope [0 1 0] [(/ gr-dur 2) (/ gr-dur 2)] :sine)
                                trigger :action FREE)
        phase        (phasor:ar :start start-pos :end end-pos :rate rate)
        signal       (* (play-buf:ar 2 buf buf-rate trigger phase) gr-env amp)]

    (out out-bus (pan2 signal))
   ))

;;;;;;;;;;;;;;;;;;;;;;;; TESTS ;;;;;;;;;;;;;;;;;;;;;;;;

(def playing-granulator (granulator digitalbuf :rate 1.5 :start 0.1))
(ctl playing-granulator :rate 1.5)

(stop)


(def digitalbuf (load-sample "digital.wav"))
(grain digitalbuf 234600)
(stereo-partial-player digitalbuf)
(stop)
(demo (sin-osc))
(playbacker digitalbuf)
(play-buf 1 digitalbuf 1 1 (buf-frames digitalbuf))
