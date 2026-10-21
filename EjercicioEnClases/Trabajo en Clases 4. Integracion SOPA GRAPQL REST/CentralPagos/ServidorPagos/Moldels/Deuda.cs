using System.Runtime.Serialization;

namespace ServidorPagos.Models
{
    [DataContract]
    public class Deuda
    {
        [DataMember]
        public int Id { get; set; }

        [DataMember]
        public string Empresa { get; set; } = "";

        [DataMember]
        public string CI { get; set; } = "";

        [DataMember]
        public string NombreCompleto { get; set; } = "";

        [DataMember]
        public int NumeroFactura { get; set; }

        [DataMember]
        public int Anio { get; set; }

        [DataMember]
        public string Mes { get; set; } = "";

        [DataMember]
        public decimal TotalFactura { get; set; }

        [DataMember]
        public string Estado { get; set; } = "";
    }
}