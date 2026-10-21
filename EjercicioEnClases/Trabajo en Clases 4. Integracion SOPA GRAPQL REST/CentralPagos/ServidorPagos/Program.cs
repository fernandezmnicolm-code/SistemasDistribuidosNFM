using CoreWCF;
using CoreWCF.Configuration;
using CoreWCF.Description;
using ServidorPagos.Services;

var builder = WebApplication.CreateBuilder(args);

// Puerto de nuestro servidor SOAP
builder.WebHost.UseUrls("http://localhost:5000");

// Registrar los servicios SOAP
builder.Services.AddServiceModelServices();
builder.Services.AddServiceModelMetadata();

// Registrar nuestra clase ServicioPagos
builder.Services.AddSingleton<ServicioPagos>();

var app = builder.Build();

// Habilitar CoreWCF
app.UseServiceModel(serviceBuilder =>
{
    serviceBuilder.AddService<ServicioPagos>();

    serviceBuilder.AddServiceEndpoint<ServicioPagos, IServicioPagos>(
        new BasicHttpBinding(),
        "/ServicioPagos.svc"
    );
});

// Habilitar WSDL
var metadata = app.Services.GetRequiredService<ServiceMetadataBehavior>();

metadata.HttpGetEnabled = true;
metadata.HttpGetUrl = new Uri("http://localhost:5000/ServicioPagos.svc");

app.Run();