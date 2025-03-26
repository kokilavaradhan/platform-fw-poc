# Copyright (C) 2025 Unknown User <kokilavaradhan@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "ibm-fw-proprietary-p10"
HOMEPAGE = "https://github.ibm.com/open-power/ibm-fw-proprietary-p10"

PV = "0.1+git${SRCPV}"
PR = "r1"
SRCREV="043e661d1e95f62e1e4ff8bdd216e2f1443019af"
SRC_URI = "git://git@github.ibm.com/open-power/ibm-fw-proprietary.git;branch=master;protocol=ssh"
S = "${WORKDIR}/git"
B = "${WORKDIR}/build"


INSANE_SKIP_${PN} += "license"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""


