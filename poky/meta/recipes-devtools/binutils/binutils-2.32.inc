LIC_FILES_CHKSUM="\
    file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552\
    file://COPYING.LIB;md5=9f604d8a4f8e74f4f5140845a21b6674\
    file://COPYING3;md5=d32239bcb673463ab874e80d47fae504\
    file://COPYING3.LIB;md5=6a6a8e020838b23406c81b19c1d46df6\
    file://gas/COPYING;md5=d32239bcb673463ab874e80d47fae504\
    file://include/COPYING;md5=59530bdf33659b29e73d4adb9f9f6552\
    file://include/COPYING3;md5=d32239bcb673463ab874e80d47fae504\
    file://libiberty/COPYING.LIB;md5=a916467b91076e631dd8edb7424769c7\
    file://bfd/COPYING;md5=d32239bcb673463ab874e80d47fae504\
    "

def binutils_branch_version(d):
    pvsplit = d.getVar('PV').split('.')
    return pvsplit[0] + "_" + pvsplit[1]

PV = "2.32"
CVE_VERSION = "2.32"
BINUPV = "${@binutils_branch_version(d)}"
#BRANCH = "binutils-${BINUPV}-branch"
BRANCH ?= "binutils-2_32-branch"
UPSTREAM_CHECK_GITTAGREGEX = "binutils-(?P<pver>\d+_(\d_?)*)"
PV = "2.32"

BINUTILS_GIT_URI ?= "git://sourceware.org/git/binutils-gdb.git;protocol=git;nobranch=1"
SRCREV = "9163eacc1840f8db0523e21d4e7bc4239608c564"
#BINUTILS_GIT_URI ?= "git://sourceware.org/git/binutils-gdb.git;branch=binutils-${BINUPV}-branch;protocol=git"
SRC_URI = "\
     ${BINUTILS_GIT_URI} \
"
S  = "${WORKDIR}/git"

do_configure:prepend () {
        rm -rf ${S}/gdb ${S}/libdecnumber ${S}/readline ${S}/sim
}
