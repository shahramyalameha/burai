SCF
===

In case users set scf condition, users select "scf" from right menu.
The scf window is consist of three windows, "Standard Setting", "Electronic Optimization", "Magnetization", and "GGA+U".

Standard Setting
----------------


.. image:: ../../img/projects/imgCreateJob_SCF00.png
   :scale: 50 %
   :align: center

|
.. csv-table:: Standard Setting
    :header: "No.", "Name", "Details"
    :widths: 10, 10, 35

    "1", "Restart Mode", "if users would like to to continue an interrupted calculation, users select yes."
    "2", "Max Time", "Job stops after users set CPU time."
    "3", "Calc. Force", "calculate forces"
    "4", "Calc. Stress", "calculate stress."
    "5", "Cutoff for W.F.", "kinetic energy cutoff for wavefunctions"
    "6", "Cutoff for Charge", "kinetic energy cutoff for charge density"
    "7", "Total Charge", "Total charge of the system. +1 means one electron lost from calculation cell. -1 means one electron add to calculation cell."
    "8", "Use Symmetry", "Use Symmetry"
    "9", "K-Points", "Monkhorst-Pack scheme"
    "10", "Occupations", ""
    "11", "Smearing", "select smearing method"
    "12", "Smearing Width", ""

Electronic Optimization
-----------------------

.. image:: ../../img/projects/imgCreateJob_SCF01.png
   :scale: 50 %
   :align: center

|
.. csv-table:: Electronic Optimization
    :header: "No.", "group", "name"
    :widths: 10, 10, 35

    "1", "Convergence", "Max Steps"
    "2", "Convergence", "Threshold"
    "3", "Wave Function", "Initial Guess"
    "4", "Wave Function", "Diagonalization"
    "5", "Wave Function", "Initial Guess"
    "6", "Charge Density", "Mixing Method"
    "7", "Charge Density", "Rate of New Chg."
    "8", "Charge Density", "Stored Charges"

Magnetization
-------------

If users would like to consider the spin for system, user handles this window.

|
.. image:: ../../img/projects/imgCreateJob_SCF02.png
   :scale: 50 %
   :align: center

GGA+U
-----

If users would like to consider the GGA+U for system, user handles this window.
|
.. image:: ../../img/projects/imgCreateJob_SCF03.png
   :scale: 50 %
   :align: center


