using CoreWCF;
using ServidorPagos.Models;

namespace ServidorPagos.Services
{
    [ServiceContract]
    public interface IServicioPagos
    {
        [OperationContract]
        Deuda[] verDeuda(
            string CI,
            string primerApellido,
            string segundoApellido,
            string nombres
        );

        [OperationContract]
        bool PagarDeuda(Deuda c);
    }
}