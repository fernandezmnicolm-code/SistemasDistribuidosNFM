<?php

namespace App\Http\Controllers;

use App\Models\Factura;
use Illuminate\Http\Request;

class FacturaController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        return response()->json(\App\Models\Factura::all());
    }


    public function buscarPorDocumento($numero_documento)
{
    $facturas = \App\Models\Factura::where(
        'numero_documento',
        $numero_documento
    )
    ->where('estado', 'emitida')
    ->get();

    return response()->json($facturas);
}

public function pagarFactura($id)
{
    $factura = \App\Models\Factura::find($id);

    if (!$factura) {
        return response()->json([
            'mensaje' => 'Factura no encontrada'
        ], 404);
    }

    if ($factura->estado !== 'emitida') {
        return response()->json([
            'mensaje' => 'La factura no está pendiente de pago'
        ], 400);
    }

    $factura->estado = 'pagada';
    $factura->save();

    return response()->json([
        'mensaje' => 'Pago realizado correctamente',
        'factura' => $factura
    ]);
}

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        //
    }

    /**
     * Display the specified resource.
     */
    public function show(Factura $factura)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, Factura $factura)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(Factura $factura)
    {
        //
    }
}
