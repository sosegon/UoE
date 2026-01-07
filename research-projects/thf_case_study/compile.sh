#!/bin/bash
pdflatex casestudy.tex
bibtex casestudy.aux
pdflatex casestudy.tex
pdflatex casestudy.tex
