(TeX-add-style-hook
 "rapport"
 (lambda ()
   (TeX-add-to-alist 'LaTeX-provided-class-options
                     '(("article" "letterpaper")))
   (TeX-add-to-alist 'LaTeX-provided-package-options
                     '(("inputenc" "utf8") ("babel" "frenchb")))
   (TeX-run-style-hooks
    "latex2e"
    "article"
    "art10"
    "natbib"
    "alifexi"
    "inputenc"
    "babel")
   (LaTeX-add-labels
    "size"
    "fig1"
    "eq4"
    "cond"
    "fig2"
    "power"
    "fig3"
    "fig4"
    "fig5")
   (LaTeX-add-bibliographies))
 :latex)

