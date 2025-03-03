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



SRC_URI = "git://github.com/open-power/hostboot;branch=release-fw1060;protocol=https"
S = "${WORKDIR}/git"
B = "${WORKDIR}/build"
inherit autotools

# Define the default commit hash (latest known stable version)
DEFAULT_SRCREV = "b573faf43f15e171e08ff5f59da98055ab7722a5"
#DEFAULT_SRCREV="1e3984c6540ba6d327d0c6f7aaccd783647ef27f"
# Allow overriding SRCREV dynamically
HOSTBOOT_P10_USE_CUSTOM_VERSION ?= "0"
HOSTBOOT_P10_CUSTOM_VERSION_VALUE ?= ""
IBM_FW_PROPRIETARY_P10_BUILD_DIR="/home/kokilav/op-bitbkae-poc/platform-fw-poc/build/hostbuild/tmp-glibc/work/powerpc64-oe-linux/ibm-fw-proprietary-p10"
IBM_FW_PROPRIETARY_P10_VERSION="043e661d1e95f62e1e4ff8bdd216e2f1443019af"

# Determine SRCREV value dynamically
SRCREV = "${@'${HOSTBOOT_P10_CUSTOM_VERSION_VALUE}' if '${HOSTBOOT_P10_USE_CUSTOM_VERSION}' == '1' else '${DEFAULT_SRCREV}'}"

PACKAGECONFIG ??= ""

PACKAGECONFIG[proprietary-fw] = ",,ibm-fw-proprietary-p10,"
SRC_URI += "file://hostboot.patch"
# Conditionally add `ibm-fw-proprietary-p10` to DEPENDS
DEPENDS += "${@bb.utils.contains('PACKAGECONFIG', 'proprietary-fw', 'ibm-fw-proprietary-p10', '', d)}"
DEPENDS += "ibm-fw-proprietary-p10"
# Dependencies for the build
DEPENDS += "libxml-simple-perl-native"
DEPENDS += "libxml-simple-perl"

DEPENDS += "bash"
PREFERRED_VERSION_binutils-native = "2.34"
# Define build task
# Set the environment variables or flags that need to be passed to the build process
# Set up paths for include and library directories

DEPENDS += "gcc"
CC = "${HOST_CC}"
CXX = "${HOST_CXX}"
LDFLAGS:remove = "-Wl,-O1"
LDFLAGS:remove = " -Wl,--hash-style=gnu "
LDFLAGS:remove = " -Wl,--as-needed "

CFLAGS:remove = "-Werror"

# Unset specific variables
unset SFC_IS_AST2500
unset SFC_IS_AST2400
unset PNORDD_IS_IPMI
unset PNORDD_IS_SFC
unset ALLOW_MICRON_PNOR
unset ALLOW_MACRONIX_PNOR
unset MVPD_READ_FROM_PNOR
unset MVPD_WRITE_TO_PNOR
unset DJVPD_READ_FROM_PNOR
unset DJVPD_WRITE_TO_PNOR
unset MEMVPD_READ_FROM_PNOR
unset MEMVPD_WRITE_TO_PNOR
unset PVPD_READ_FROM_HW
unset PVPD_WRITE_TO_HW
unset PVPD_READ_FROM_PNOR
unset PVPD_WRITE_TO_PNOR
unset PALMETTO_VDDR
unset PCIE_HOTPLUG_CONTROLLER
unset CONSOLE_OUTPUT_TRACE
unset HANG_ON_MFG_SRC_TERM
unset PNOR_TWO_SIDE_SUPPORT
unset FORCE_SINGLE_CHIP
unset ENABLE_TOD_REDUNDANCY

# Add custom configurations to the configure step
EXTRA_OECONF += "FILE_XFER_VIA_PLDM"
EXTRA_OECONF += "MVPD_READ_FROM_HW"
EXTRA_OECONF += "MVPD_WRITE_TO_HW"
EXTRA_OECONF += "DJVPD_READ_FROM_HW"
EXTRA_OECONF += "DJVPD_WRITE_TO_HW"
EXTRA_OECONF += "MEMVPD_READ_FROM_HW"
EXTRA_OECONF += "MEMVPD_WRITE_TO_HW"
EXTRA_OECONF += "GPIODD"
EXTRA_OECONF += "SBE_UPDATE_CONSECUTIVE"
EXTRA_OECONF += "AGGRESSIVE_LRU"
EXTRA_OECONF += "CONSOLE"
EXTRA_OECONF += "BMC_AST2500"
EXTRA_OECONF += "BMC_BT_LPC_IPMI"
EXTRA_OECONF += "AXONE"
EXTRA_OECONF += "AXONE_BRINGUP"
EXTRA_OECONF += "SUPPORT_EEPROM_CACHING"
EXTRA_OECONF += "SUPPORT_EEPROM_HWACCESS"
EXTRA_OECONF += "CONSOLE_OUTPUT_FFDCDISPLAY"
EXTRA_OECONF += "PRINT_SYSTEM_INFO"
EXTRA_OECONF += "ENABLE_HDAT_IN_HOSTBOOT"
EXTRA_OECONF += "P10_BRING_UP"
EXTRA_OECONF += "LOAD_LIDS_VIA_PLDM"
EXTRA_OECONF += "COMPILE_VPD_ECC_ALGORITHMS"



do_compile() {
    export PERL5LIB="/home/kokilav/op-bitbkae-poc/platform-fw-poc/build/hostbuild/tmp-glibc/work/powerpc64-oe-linux/libxml-simple-perl/2.25/XML-Simple-2.25/lib"    
export PROJECT_ROOT="${S}"
export CROSS_PREFIX="powerpc64-oe-linux-"
export HOST_PREFIX=""
export JAILCMD=""
cd ${S}
export OPENPOWER_BUILD=1
export SKIP_BINARY_FILES=1
export COMPILETIME_TRACEHASH=1
export CC="/usr/bin/gcc-8"
export CXX="/usr/bin/g++-8"
export HOST_BINUTILS_DIR="/home/kokilav/op-bitbkae-poc/platform-fw-poc/build/hostbuild/tmp-glibc/sysroots-components/x86_64/binutils-native/usr"
# Set the sysroot path (adjust the path as needed)

# Add the sysroot to the CFLAGS and CXXFLAGS
# Append to CFLAGS and CXXFLAGS in Yocto recipe


   # HOSTBOOT_PRECOMPILED_LIBRARIES="/home/kokilav/op-bitbkae-poc/platform-fw-poc/build/hostbuild/tmp-glibc/work/powerpc64-oe-linux/hostboot/0.1+git/git/obj/modules/ecc/"

    #if [ -f ${HOSTBOOT_PRECOMPILED_LIBRARIES}/libecc_static.a ]; then
     #   echo "Using precompiled library for ECC implementation"
     #   # Proceed with the default compile using precompiled library
    #else
     #   echo "Building ECC from source using ibm-fw-proprietary-p10"
        # Handle compilation from source if the library doesn't exist
    #    DEPENDS += "ibm-fw-proprietary-p10"
    #fi
    # Handling the symlink workaround
    if ! cmp --quiet src/include/usr/trace/interface.H src/include/usr/tracinterface.H; then
        rm -f src/include/usr/tracinterface.H
        cp src/include/usr/trace/interface.H src/include/usr/tracinterface.H
    fi

    # Handle proprietary files only if the package is enabled
    if [ -n "${IBM_FW_PROPRIETARY_P10_BUILD_DIR}" ]; then
        cp --no-clobber ${IBM_FW_PROPRIETARY_P10_BUILD_DIR}/0.1+git/git/vpd/* /home/kokilav/op-bitbkae-poc/platform-fw-poc/build/hostbuild/tmp-glibc/work/powerpc64-oe-linux/hostboot/0.1+git/git/src/usr/vpd
        mkdir -p  /home/kokilav/op-bitbkae-poc/platform-fw-poc/build/hostbuild/tmp-glibc/work/powerpc64-oe-linux/hostboot/0.1+git/git/src/build/tools/extern/ibm-fw-proprietary/
        cp --no-clobber -r ${IBM_FW_PROPRIETARY_P10_BUILD_DIR}/0.1+git/git/vpd/*  /home/kokilav/op-bitbkae-poc/platform-fw-poc/build/hostbuild/tmp-glibc/work/powerpc64-oe-linux/hostboot/0.1+git/git/src/build/tools/extern/ibm-fw-proprietary/
        echo ${IBM_FW_PROPRIETARY_P10_VERSION} > /home/kokilav/op-bitbkae-poc/platform-fw-poc/build/hostbuild/tmp-glibc/work/powerpc64-oe-linux/hostboot/0.1+git/git/src/build/tools/extern/ibm-fw-proprietary/LIBECC_COMMIT_HASH
    fi
    oe_runmake BUILD_VERBOSE=1
    
}
do_compile:prepend() {
    sed -i '1s|#!/bin/sh|#!/bin/bash|' ${S}/src/build/mkrules/binfile.rules.mk
}
# Install command for the generated images
do_install() {
    cd ${S}
}

