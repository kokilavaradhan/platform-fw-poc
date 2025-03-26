# Copyright (C) 2025 Unknown User <kokilavaradhan@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "hostboot"
HOMEPAGE = "https://github.com/open-power/hostboot"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM ="file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"
SECTION = "utils"
DEPENDS = "fsp-trace binutils binutils-native"
PV = "0.1+git${SRCPV}"
PR = "r1"

# Dependencies
DEPENDS += "fsp-trace binutils binutils-native libxml-simple-perl-native libxml-simple-perl gcc"

# Preferred version of binutils-native
PREFERRED_VERSION_binutils-native = "2.3%"

#source code and patches
SRC_URI = "git://github.com/open-power/hostboot;branch=release-fw1060;protocol=https"
SRC_URI += "file://p10ebmc.config"
SRC_URI += "file://hostboot.patch"
SRC_URI += "file://ecc.patch"
S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

inherit autotools

# Work directory paths
BASE_WORKDIR := "${TMPDIR}/work/${TARGET_SYS}"
IBM_FW_PROPRIETARY_P10_BUILD_DIR := "${BASE_WORKDIR}/ibm-fw-proprietary-p10"

#Define the default commit hash (latest known stable version)
DEFAULT_SRCREV = "b573faf43f15e171e08ff5f59da98055ab7722a5"
HOSTBOOT_P10_USE_CUSTOM_VERSION ?= "0"
HOSTBOOT_P10_CUSTOM_VERSION_VALUE ?= ""
IBM_FW_PROPRIETARY_P10_VERSION = "043e661d1e95f62e1e4ff8bdd216e2f1443019af"
# Determine SRCREV dynamically
SRCREV = "${@'${HOSTBOOT_P10_CUSTOM_VERSION_VALUE}' if '${HOSTBOOT_P10_USE_CUSTOM_VERSION}' == '1' else '${DEFAULT_SRCREV}'}"

#Package Configuration
PACKAGECONFIG += "proprietary-fw"

PACKAGECONFIG[proprietary-fw] = ",,ibm-fw-proprietary-p10,"
# Conditionally add `ibm-fw-proprietary-p10` to DEPENDS
DEPENDS += "${@bb.utils.contains('PACKAGECONFIG', 'proprietary-fw', 'ibm-fw-proprietary-p10', '', d)}"

#Compiler and linker flags 
LDFLAGS:remove = "-Wl,-O1"
LDFLAGS:remove = " -Wl,--hash-style=gnu "
LDFLAGS:remove = " -Wl,--as-needed "

CFLAGS:remove = "-Werror"

do_configure() {
    # Copy config file to work directory
    install -m 0644 ${WORKDIR}/p10ebmc.config ${S}/
}

#EXTRA_OEMAKE += "SHELL=/bin/bash"
do_compile() {
    # Ensure all required environment variables are set
    export PERL5LIB="${BASE_WORKDIR}/libxml-simple-perl/2.25/XML-Simple-2.25/lib"
    export PROJECT_ROOT="${S}"
    export CROSS_PREFIX="powerpc64-oe-linux-"
    export HOST_PREFIX=""
    export JAILCMD=""
    export OPENPOWER_BUILD=1
    export SKIP_BINARY_FILES=1
    export COMPILETIME_TRACEHASH=1
    export HOST_BINUTILS_DIR="${TMPDIR}/sysroots-components/x86_64/binutils-native/usr/"
    export SHELL="/bin/bash"
    export BASH="/bin/bash"
    cd ${S}
    if [ -n "${IBM_FW_PROPRIETARY_P10_BUILD_DIR}" ]; then
        SRC_DIR="${IBM_FW_PROPRIETARY_P10_BUILD_DIR}/0.1+git/git/vpd"
        DEST_DIR="${WORKDIR}/git/src/usr/vpd"
        EXTERN_DIR="${WORKDIR}/git/src/build/tools/extern/ibm-fw-proprietary"
        # Ensure destination directories exist
        mkdir -p "${DEST_DIR}" "${EXTERN_DIR}"
        # Copy files safely
        cp --no-clobber "${SRC_DIR}"/* "${DEST_DIR}/"
	cp --no-clobber -r "${SRC_DIR}/." "${EXTERN_DIR}/"
        # Write version info
        echo "${IBM_FW_PROPRIETARY_P10_VERSION}" > "${EXTERN_DIR}/LIBECC_COMMIT_HASH"
    fi
	EXTRA_OEMAKE="COMMIT_HASH='$(cd ${S}/src/build/tools/extern/ibm-fw-proprietary/vpd/.. && \
    (git --git-dir=.git rev-parse HEAD 2>/dev/null || cat LIBECC_COMMIT_HASH 2>/dev/null))'"
    # Run compilation
    oe_runmake CONFIG_FILE=${S}/p10ebmc.config  ${EXTRA_OEMAKE} V=1
}

do_compile:prepend() {
    sed -i '1s|#!/bin/sh|#!/bin/bash|' ${S}/src/build/mkrules/binfile.rules.mk
}

# Install command for the generated images
do_install() {
    cd ${S}
}

