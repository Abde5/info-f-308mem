(use 'overtone.core)
(connect-external-server 57110)

;; Time Stretching following the Phase Vocoder Algorithm
;; -> I don't think so
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


;; Granular live time stretching algorithm

(defsynth grain [buf 1 startpos 0.0 dur 0.02 stretchrate 1]
  "This synth will play a grain of the buffer stored in buf.
      - buf : buffer number where sample is located.
      - dur : duration in s (default 20ms).
      - "
  (let [rate     (buf-rate-scale buf)
        n-frames (buf-frames buf)
        env      (- 0.001 (env-gen (perc 0.01 dur) :action FREE))
        phase    (phasor:ar :start 0 :end n-frames :rate stretchrate)
        frames   (* phase n-frames)
        gr       (play-buf 2 buf rate 1 frames)]
    (out 0 (* gr env)))
  )
(demo (sin-osc))
((sample "digital.wav"))
(stop)

(defsynth playbacker [buf 1]
  (out 0 (play-buf 1 buf)))


(def digitalbuf (load-sample "digital.wav"))
(stereo-partial-player digitalbuf)
(stop)
(demo (sin-osc))
(playbacker digitalbuf)
(play-buf 1 digitalbuf 1 1 (buf-frames digitalbuf))
