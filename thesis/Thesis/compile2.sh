#!/bin/bash
pdflatex mainT.tex
bibtex mainT.aux
pdflatex mainT.tex
pdflatex mainT.tex
