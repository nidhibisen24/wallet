from django.contrib import admin

from django.contrib import admin
from .models import User, Wallet, FundRequest , QRCode , ChatRoom , Message , SavedPaymentDetails


@admin.register(User)
class UserAdmin(admin.ModelAdmin):
    list_display = (
        'id',
        'mobile_number',
        'full_name',
        'role',
        'is_active',
        'created_at'
    )

    search_fields = (
        'mobile_number',
        'full_name'
    )

    list_filter = (
        'role',
        'is_active'
    )


@admin.register(Wallet)
class WalletAdmin(admin.ModelAdmin):
    list_display = (
        'id',
        'user',
        'balance',
        'updated_at'
    )

    search_fields = (
        'user__mobile_number',
    )


@admin.register(FundRequest)
class FundRequestAdmin(admin.ModelAdmin):
    list_display = (
        'id',
        'user',
        'amount',
        'request_type',
        'status',
        'created_at'
    )

    search_fields = (
        'user__mobile_number',
    )

    list_filter = (
        'request_type',
        'status'
    )

@admin.register(QRCode)
class QRCodeAdmin(admin.ModelAdmin):
    list_display = (
        "id",
        "image",
        "uploaded_at"
    )

    search_fields = (
        "id",
    )

    readonly_fields = (
        "uploaded_at",
    )

admin.site.register(ChatRoom)
admin.site.register(Message)
admin.site.register(SavedPaymentDetails)