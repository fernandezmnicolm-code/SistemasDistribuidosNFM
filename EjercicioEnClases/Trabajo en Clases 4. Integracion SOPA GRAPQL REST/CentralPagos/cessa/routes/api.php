<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\FacturaController;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');

Route::apiResource('facturas', FacturaController::class);
Route::get('facturas/documento/{numero_documento}',[FacturaController::class, 'buscarPorDocumento']);
Route::patch('facturas/{id}/pagar',[FacturaController::class, 'pagarFactura']);
