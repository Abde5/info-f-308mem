(use 'overtone.core)
(connect-external-server 57110)

;; Granular live time stretching algorithm (AKA SOLA)
(defsynth granulator
  "commentaire à écrire"
  [buf 0 rate 1 gr-dur 0.12 gr-delay 0.6 tr-dur 0.3 start 0 end 1 out-bus 0 loop? 0 amp 1]
  (let [n-frames     (buf-frames buf)
        buf-rate     (buf-rate-scale buf)
        rate         (* rate buf-rate)
        start-pos    (* start n-frames)
        end-pos      (* end n-frames)
        trigger      (trig1:kr (sin-osc:kr ( / 1 (* gr-dur gr-delay))) (* gr-dur tr-dur))
        gr-env       (env-gen:kr (envelope [0 1 0] [(/ gr-dur 2) (/ gr-dur 2)] :sine)
                                trigger :action FREE)
        phase        (phasor:ar :start start-pos :end end-pos :rate rate)
        signal       (* (play-buf:ar 2 buf buf-rate trigger phase) gr-env amp)]

    (out out-bus (pan2 signal))
    )
  )

;;;;;;;;;;;;;;;;;;;;;;;; TESTS ;;;;;;;;;;;;;;;;;;;;;;;;

;; load buffer with wav
(def digitalbuf (load-sample "digital.wav"))
;; play sample with the granulator
(def playing-granulator (granulator digitalbuf))
;; control parameters real-time (rate, delay between grains, etc...)
(ctl playing-granulator :rate 1.4 :gr-dur 0.3 :gr-delay 0.2 :tr-dur 0.1)

(stop)
