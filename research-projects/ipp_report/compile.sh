#!/bin/bash
pdflatex ipp_report.tex
bibtex ipp_report.aux
pdflatex ipp_report.tex
pdflatex ipp_report.tex
