Nombre: Fernandez Menacho Nicol Milena
----------------------------
**clienteTurista**: no hice funcionar como cliente, solo anote atributos, constructor y comprarTour() pero me faltab el main(), locateRegistry.getRegistry() , lookup() y la llamada remota .

**IntServidor/IServidor**: esta vacia, hace extends pero no contiene COmprar Tour( )throws REmoteException 

**Operador**: se extiende UnicastRemoteObject pero no contiene COmprarTour

**Pago**: aqui solo me faltaba Serializable, como pago se devuelve mediante RMI  debe ser serializable.

**Voucher**: Aqui tampoco implemente Serializable y eso produce justamente el  NotSerializableException.

**ServidorOperador**: aqui me falataba crear el registro RMI y publicar el objeto OPerador con rebind.

*FALTABA IMPLEMENTAR LAS INTERFCAES DE BANCO, ANTIFRAUDE, MIGRACION*
