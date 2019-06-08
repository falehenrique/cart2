#!/bin/bash
gs -dBATCH -dPDFA -dNOOUTERSAVE -sPAPERSIZE=a4 -dFIXEDMEDIA -dPDFFitPage -sProcessColorModel=DeviceRGB -dQUIET -sDEVICE=pdfwrite -o $2 -dPDFACompatibilityPolicy=1 $1
