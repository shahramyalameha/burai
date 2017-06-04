Optimize
========

In case users set the condition of optimization, users select "opt" from right menu.
Also, users should set the condition after or before user set scf condition.


.. image:: ../../../img/input_editor/imgCreateJob_OPT00.png
   :scale: 50 %
   :align: center

|
.. csv-table:: Optimize
    :header: "No.", "Name", "Details"
    :widths: 10, 10, 35

    "1", "Restart Mode", "if users would like to to continue an interrupted calculation, users select yes."
    "2", "Max Time", "Job stops after users set CPU time."
    "3", "Max Steps", "number of structural optimization steps"
    "4", "Variable Cell", "whether optimize cell size or not"
    "5", "Threshold", "Convergence threshold on forces for ionic minimization"
    "6", "Method", "the type of ionic dynamics"
    "7", "Pressure", "Target pressure"
    "8", "Freegom", "Select which of the cell parameters should be moved"

if Variable Cell is "yes", users can input Threshold, Method, Pressure, and Freedom.


.. image:: ../../../img/input_editor/imgCreateJob_OPT01.png
   :scale: 50 %
   :align: center

In addition, users can conform input condition form input-file of left menue.


.. image:: ../../../img/input_editor/imgCreateJob_CheckInputFIle.png
   :scale: 50 %
   :align: center

