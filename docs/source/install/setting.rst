Setting
=======

This page explains how to install Quantum ESPRESSO and BURAI.

Quantum ESPRESSO
----------------

* Windows and Mac

Users do not need to install Quantum ESPRESSO, because BURAI system already has the Quantum ESPRESSO executables.
These executables were compiled by BURAI team.
if users want to use other Quantum ESPRESSO version, the users should install it.


* Linux

Instructions for the impatient:

| tar -xzvf espresso-X.Y.Z.tar.gz
| cd espresso-X.Y.Z/
| ./configure
| make all|
| make install

If users change the installed directory, user should set " --with-qe-source" of configure option.
(./configure --with-qe-source=$HOME/espresso-X.Y.Z)
if you will use in parallel by using MPI, you should install MPI.

.. warning::

	The current BURAI platform version does not support for LINUX system.


BURAI
-----

After :doc:`downloading <./download.rst>`, you unzip the file to any directory on your computer.
The unzipped directory contains BURAI.exe file. The file boot up the BURAI platform.

.. note::

    BURAI platform need JRE1.8 or later version. Therefore, you need to install these before you start BURAI platform.

