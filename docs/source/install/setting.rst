Setting
=======

Quantum ESPRESSO
----------------

* Linux

Instructions for the impatient:

    cd espresso-X.Y.Z/
    ./configure
     make all
     make install

If users change the installed directory, user should set " --with-qe-source" of configure option.
(./configure --with-qe-source=$HOME/espresso-X.Y.Z)
|
if you will use in parallel by using MPI, you should install MPI.

* Windows


* Mac
now making


BURAI
-----

You get the compressed BURAI file from `the download page <http://nisihara.wixsite.com/burai>`_ ,
then you unzip the file in the directory which you like.
The unzipped directory has BURAI.exe. Single or Double clicking on BURAI.exe starts BURAI system.
This system requires JRE1.8 or later version of runtime environment,
so you need to install these before using this system.

