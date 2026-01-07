#!/bin/bash
pdflatex irr-report.tex
bibtex irr-report.aux
pdflatex irr-report.tex
pdflatex irr-report.tex