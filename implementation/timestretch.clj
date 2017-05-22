(use 'overtone.core)
(connect-external-server 57110)

(use 'overtone.inst.drum)

;; Granular live time stretching algorithm (AKA yOLA)
(defsynth granulator
  "commentaire à écrire"
  [buf 0 rate 1 gr-dur 0.12 gr-delay 0.25 tr-dur 0.15 start 0 end 1 out-bus 0 loop? 0 amp 1]
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
(def digitalbuf (load-sample "eternity.wav"))
;; play sample with the granulator
(def playing-granulator (granulator digitalbuf))


;; control parameters real-time (rate, delay between grains, etc...)
; smash
(ctl playing-granulator :gr-dur 0.15 :gr-delay 0.5 :tr-dur 0.1)

; daft
(ctl playing-granulator :gr-dur 0.2 :gr-delay 0.6 :tr-dur 0.1)

; vivaldi
(ctl playing-granulator :gr-dur 0.33 :gr-delay 0.39 :tr-dur 0.36)

; metallica
(ctl playing-granulator :gr-dur 0.23 :gr-delay 0.24 :tr-dur 0.36)

(stop)


;;;;;;;;;;;; MPD bindings for realtime parameter modify
(on-event [:midi :control-change]
          (fn [e]

            (let [note (:data2 e)
                  controller (:note e)]
              (cond (= controller 3)
                    (do (println "scale: " (float (/ note 64)))
                        (ctl playing-granulator :rate (/ note 64)))

                    (= controller 9)
                    (do (println "grain duration: " (float (/ note 300)))
                        (ctl playing-granulator :gr-dur (/ note 300) ))

                    (= controller 12)
                    (do (println "grain delay: " (float (/ note 200)))
                        (ctl playing-granulator :gr-dur (/ note 200) ))

                    (= controller 13)
                    (do (println "trigger duration: " (float (/ note 350)))
                        (ctl playing-granulator :gr-dur (/ note 350) ))
                    )
            )
          )
     ::MPDtimescale
)
;;;;;;;;;;;;;;;;
