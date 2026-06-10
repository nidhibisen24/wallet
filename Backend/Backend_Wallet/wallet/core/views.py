from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.contrib.auth.hashers import check_password

from .models import User, Wallet , FundRequest ,QRCode
from .serializers import RegisterSerializer ,QRCodeSerializer,FundApprovedSerializers ,FundRequestSerializer, TransactionHistorySerializer,UserDashboardSerializer , UserListSerializer , UserRequestHistorySerializer,UserDetailSerializer
from django.contrib.auth import authenticate
from django.shortcuts import get_object_or_404
from rest_framework import status



#Register or SignUp
@api_view(['POST'])
def register(request):

    serializer = RegisterSerializer(data=request.data)

    if not serializer.is_valid():
        return Response(
            serializer.errors,
            status=400)

    full_name = serializer.validated_data['full_name']
    mobile_number = serializer.validated_data['mobile_number']
    password = serializer.validated_data['password']

    if User.objects.filter(mobile_number=mobile_number).exists():

        return Response({
            "error": "Mobile number already exists"
        }, status=400)

    user = User.objects.create_user(
        mobile_number=mobile_number,
        password=password
    )

    user.full_name = full_name
    user.save()

    Wallet.objects.create(
        user=user
    )

    return Response({
        "message": "User registered successfully",
        "user_id": user.id
    })


#Login
@api_view(['POST'])
def login(request):

    mobile_number = request.data.get('mobile_number')
    password = request.data.get('password')

    if not mobile_number or not password:
        return Response(
            {"error": "Mobile number and password required"},
            status=400
        )

    user = authenticate(
        request,
        mobile_number=mobile_number,
        password=password
    )

    if user is None:
        return Response(
            {"error": "Invalid credentials"},
            status=401
        )

    wallet = Wallet.objects.get(user=user)

    return Response({
        "message": "Login successful",
        "user_id": user.id,
        "full_name": user.full_name,
        "mobile_number": user.mobile_number,
        "role": user.role,
        "wallet_balance": wallet.balance
    })

#Delete User
@api_view(['DELETE'])
def delete_user(request, user_id):

    try:
        user = User.objects.get(id=user_id)

        user.delete()

        return Response(
            {
                "message": "User deleted successfully"
            },
            status=status.HTTP_200_OK
        )

    except User.DoesNotExist:

        return Response(
            {
                "error": "User not found"
            },
            status=status.HTTP_404_NOT_FOUND
        )

#Add Fund Request
@api_view(['POST'])
def add_fund_request(request):

    data = request.data.copy()
    data['request_type'] = 'ADD'

    serializer = FundRequestSerializer(data=data)

    if serializer.is_valid():
        serializer.save()

        return Response({
            "message": "Add fund request submitted"
        })

    return Response(
        serializer.errors,
        status=400
    )

#Withdraw Fund Request

@api_view(['POST'])
def withdraw_fund_request(request):

    data = request.data.copy()
    data['request_type'] = 'WITHDRAW'

    serializer = FundRequestSerializer(data=data)

    if serializer.is_valid():
        serializer.save()

        return Response({
            "message": "Withdraw fund request submitted"
        })

    return Response(
        serializer.errors,
        status=400
    )


#FOr Approve Request
@api_view(['POST'])
def approve_request(request):

    request_id = request.data.get('request_id')

    try:
        fund_request = FundRequest.objects.get(
            id=request_id,
            status='PENDING'
        )

    except FundRequest.DoesNotExist:
        return Response(
            {
                "error": "Request not found"
            },
            status=404
        )

    wallet = Wallet.objects.get(
        user=fund_request.user
    )

    if fund_request.request_type == 'ADD':

        wallet.balance += fund_request.amount

        request_type = 'ADD'

    else:

        wallet.balance -= fund_request.amount

        request_type = 'WITHDRAW'

    wallet.save()

    FundRequest.objects.create(
        user=fund_request.user,
        amount=fund_request.amount,
        request_type=request_type,
        status='APPROVED'
    )

    fund_request.status = 'APPROVED'
    fund_request.save()

    return Response({
        "message": "Request approved successfully"
    })

#FOr Reject Request
@api_view(['POST'])
def reject_request(request):

    request_id = request.data.get('request_id')

    try:
        fund_request = FundRequest.objects.get(
            id=request_id,
            status='PENDING'
        )

    except FundRequest.DoesNotExist:
        return Response(
            {
                "error": "Request not found"
            },
            status=404
        )

    fund_request.status = 'REJECTED'
    fund_request.save()

    return Response({
        "message": "Request rejected successfully"
    })

#Approved Request
@api_view(['GET'])
def approved_requests(request):

    requests = FundRequest.objects.filter(
        status='APPROVED'
    ).order_by('-created_at')

    serializer = FundApprovedSerializers(
        requests,
        many=True
    )

    return Response(serializer.data)

#Admin DashBoard 
@api_view(['GET'])
def admin_dashboard(request):

    total_users = User.objects.count()

    pending_requests = FundRequest.objects.filter(
        status='PENDING'
    ).count()

    approved_requests = FundRequest.objects.filter(
        status='APPROVED'
    ).count()

    rejected_requests = FundRequest.objects.filter(
        status='REJECTED'
    ).count()

    return Response({
        "total_users": total_users,
        "pending_requests": pending_requests,
        "approved_requests": approved_requests,
        "rejected_requests": rejected_requests
    })

#Pending Request 
@api_view(['GET'])
def pending_requests(request):

    requests = FundRequest.objects.filter(
        status='PENDING'
    ).order_by('-created_at')

    serializer = FundRequestSerializer(
        requests,
        many=True
    )

    return Response(serializer.data)

#All Users list 
@api_view(['GET'])
def all_users(request):

    users = User.objects.all().order_by('-id')

    serializer = UserListSerializer(
        users,
        many=True
    )

    return Response(serializer.data)

#User Details
@api_view(['GET'])
def user_details(request, id):

    user = get_object_or_404(
        User,
        id=id
    )

    serializer = UserDetailSerializer(user)

    return Response(serializer.data)

#Search User by mobile number
@api_view(['GET'])
def search_user(request):

    mobile = request.GET.get('mobile')

    if not mobile:
        return Response(
            {
                "error": "Mobile number required"
            },
            status=400
        )

    users = User.objects.filter(
        mobile_number__icontains=mobile
    )

    serializer = UserListSerializer(
        users,
        many=True
    )

    return Response(serializer.data)


#User Fund Request History
@api_view(['GET'])
def user_request_history(request, id):

    user = get_object_or_404(
        User,
        id=id
    )

    requests = FundRequest.objects.filter(
        user=user
    ).order_by('-created_at')

    serializer = UserRequestHistorySerializer(
        requests,
        many=True
    )

    return Response(serializer.data)



#USer DashBoard 
@api_view(['GET'])
def user_dashboard(request, id):

    user = get_object_or_404(
        User,
        id=id
    )

    serializer = UserDashboardSerializer(user)

    return Response(serializer.data)







#Most important thing QRCODE 

#Upload QR CODE
@api_view(['POST'])
def upload_qr_code(request):

    serializer = QRCodeSerializer(
        data=request.data
    )

    if serializer.is_valid():

        QRCode.objects.all().delete()

        serializer.save()

        return Response({
            "message": "QR Code uploaded successfully"
        })

    return Response(
        serializer.errors,
        status=400
    )

# View QR Code 
@api_view(['GET'])
def get_qr_code(request):

    qr = QRCode.objects.last()

    if not qr:
        return Response({
            "error": "QR Code not found"
        })

    return Response({
        "image": request.build_absolute_uri(
            qr.image.url
        )
    })

#History 
@api_view(["GET"])
def all_transactions(request):

    transactions = FundRequest.objects.all().order_by("-created_at")

    serializer = TransactionHistorySerializer(
        transactions,
        many=True
    )

    return Response(serializer.data)