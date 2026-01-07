#!/bin/bash
pdflatex usability_report.tex
bibtex usability_report.aux
pdflatex usability_report.tex
pdflatex usability_report.tex
