Geometry
========

The geometry window is consist of three windows, "Cell", "Elemant", and "Atoms".
The geometry window shows the crystallographic information which user select.
Also, the information of used psudopotential is shown.

Cell
----

Cell window shows the lattice constants and lattice vectors.
if users change the lattice constants, users directly enter volume into the lattice constant's columns.

|
.. image:: ../../img/input_editor/imgCreateJob_Geometry00.png
   :scale: 50 %
   :align: center

|
Elements window shows the element information of selected crystal structure.
Also, in this window, users can select the pseudopotential for each element.

|
.. image:: ../../img/input_editor/imgCreateJob_Geometry01.png
   :scale: 50 %
   :align: center

|
Atoms window shows the atomic positions of selected crystal structure in the table.
Select the column of the table, the atom which are selected, becomes a mesh sphere.
.. image:: ../../img/input_editor/imgCreateJob_Geometry02.png
   :scale: 50 %
   :align: center

|
In the case that users enter the volume into the column, it is possible to use a numerical expression and pi in BURAI system.
|
.. image:: ../../img/input_editor/imgCreateJob_Geometry03.png
   :scale: 50 %
   :align: center

|
.. image:: ../../img/input_editor/imgCreateJob_Geometry04.png
   :scale: 50 %
   :align: center


Elements
--------

Elements window shows the kind of elements and psudopotential information.
If users would like to change the psudopotential, users push the button of each element in the table of Element's Properties.
Subsequently, users should select the psudopotential that users would like to use to calculate.

|
.. image:: ../../img/input_editor/imgCreateJob_Geometry05.png
   :scale: 50 %
   :align: center

When users select psudopotential, by selecting conditions of P.P. Type or XC Functional, users can narrow down the psudopotential.

.. image:: ../../img/input_editor/imgCreateJob_Geometry06.png
   :scale: 50 %
   :align: center
|
.. image:: ../../img/input_editor/imgCreateJob_Geometry07.png
   :scale: 50 %
   :align: center
|
.. image:: ../../img/input_editor/imgCreateJob_Geometry08.png
   :scale: 50 %
   :align: center

The psudopotential detail information was shown in the psudopotential conditions area.


Aroms
-----

Atoms windows shows the coordinate and element of all atom.

.. image:: ../../img/input_editor/imgCreateJob_Geometry09.png
   :scale: 50 %
   :align: center

In the case that users select a column in the table of Atomic Configuration, the atom sphere that is selected, changes the sphere of grid mesh.
|
.. image:: ../../img/input_editor/imgCreateJob_Geometry10.png
   :scale: 50 %
   :align: center

The notation of atomic coordinate can be selected in "Alat", "Bohr", "Angstrom", and "Crystal".

If users would like to delete or fix the atom, users should select atom in the table.
Subsequently, users right-click and select delete or fix from menu.

.. image:: ../../img/input_editor/imgCreateJob_Geometry11.png
   :scale: 50 %
   :align: center

If users would like to add atom in the calculation model, users should push + mark in the table.
Subsequently, users select element that user would like to enter into the calculation model, and input its coordinate.

.. image:: ../../img/input_editor/imgCreateJob_Geometry12.png
   :scale: 50 %
   :align: center

.. image:: ../../img/input_editor/imgCreateJob_Geometry13.png
   :scale: 50 %
   :align: center

.. image:: ../../img/input_editor/imgCreateJob_Geometry14.png
   :scale: 50 %
   :align: center


